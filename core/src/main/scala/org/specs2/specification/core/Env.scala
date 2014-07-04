package org.specs2
package specification
package core

import main.Arguments
import org.specs2.execute.AsResult
import reporter.LineLogger
import LineLogger._
import io._
import control._
import process.{Executor, DefaultExecutor, StatisticsRepository, Selector, DefaultSelector}

case class Env(arguments: Arguments           = Arguments(),
          indentationSize: Int           = 2,

          /** selector class */
          selectorInstance: Arguments => Selector = (arguments: Arguments) =>
            Arguments.instance(arguments.select.selector).getOrElse(DefaultSelector),

          /** executor instance */
          executorInstance: Arguments => Executor = (arguments: Arguments) =>
            Arguments.instance(arguments.execute.executor).getOrElse(DefaultExecutor),

          /** default console logger */
          lineLogger: LineLogger = NoLineLogger,

          /** default statistics repository */
          statsRepository: Arguments => StatisticsRepository = (arguments: Arguments) =>
             StatisticsRepository.file(arguments.commandLine.value("stats.outdir").getOrElse("target/specs2-reports/stats")),

          /** execution environment */
          executionEnvironment: Arguments => ExecutionEnv = (arguments: Arguments) =>
            ExecutionEnv(arguments),

          /** logger for issues */
          systemLogger: Logger = noLogging,

          /** random generator */
          random: scala.util.Random = new scala.util.Random,

          /** file system interface */
          fileSystem: FileSystem = FileSystem) {

  lazy val statisticsRepository: StatisticsRepository =
    statsRepository(arguments)

  lazy val selector = selectorInstance(arguments)
  lazy val executor = executorInstance(arguments)

  lazy val executionEnv = executionEnvironment(arguments)

  /** shutdown computing resources like thread pools */
  def shutdown = executionEnv.shutdown

  /** set new LineLogger */
  def setLineLogger(logger: LineLogger) = {
    shutdown
    copy(lineLogger = logger)
  }

  /** set new arguments */
  def setArguments(args: Arguments) = {
    shutdown
    copy(arguments = args)
  }

  /** @return an isolated env */
  def setWithoutIsolation = {
    shutdown
    copy(executionEnvironment = (arguments: Arguments) => executionEnvironment(arguments).setWithoutIsolation)
  }

  /** set a new statistic repository */
  def setStatisticRepository(repository: StatisticsRepository) = {
    shutdown
    copy(statsRepository = (args: Arguments) => repository)
  }

  /** set a new execution environment */
  def setExecutionEnv(env: ExecutionEnv) = {
    shutdown
    copy(executionEnvironment = (args: Arguments) => env)
  }
}

object Env {
  def apply(execEnv: ExecutionEnv) =
    new Env().setExecutionEnv(execEnv)

  def executeResult[R: AsResult](r: Env => R) = {
    val env = Env()
    try AsResult(r(env))
    finally env.shutdown
  }
}