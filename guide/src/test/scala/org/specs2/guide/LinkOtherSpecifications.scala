package org.specs2
package guide

object LinkOtherSpecifications extends UserGuidePage { def is = s2"""

For some large projects, or to write documentation, you will need to structure your specifications so that some of them will reference others. Those references will be of 2 type. The first type is a simple textual reference, with an html link to navigate to the other specification when you create an html report. The second type is as an executed reference where the second specification will be executed and its status reported in the first one.

Here is the DSL you will use for those 2 types of references:${snippet{
object FirstSpecification extends Specification { def is = s2"""
 Then we can consider one example
  ${ 1 must_== 1 }

  And all these examples are also important so we need to know if they all pass
  ${"important specification" ~/ SecondSpecification}

  Finally it is worth having a look at ${"this specification" ~/ ThirdSpecification}.
"""
}

import org.specs2.specification.core._

object SecondSpecification extends Specification { def is = s2"""
 This spec contains lots of examples
   ${ (1 to 100).repeat { i => "example "+i ! ok } }
"""
}
object ThirdSpecification extends Specification { def is = s2"""
 This is the third specification with a simple example
   this should pass $ok
"""
}

}}

The syntax shown above to create links is using a string for the link alias and

 - `~` for a `link` reference, the referenced specification gets executed when the first one is
 - `~/` for a `see` reference, the referenced specification doesn't get executed

Also, for better html rendering, you can add a tooltip:${snippet{
// 8<--
object OtherSpec extends Specification { def is = """nothing""" }
class s extends Specification { def is = s2""" // 8<--
  ${ "alias" ~/ (OtherSpec, "tooltip") }
// 8<--
"""
}
}}

Finally I'm also drawing your attention to the fact that you don't have to create your specifications as Scala classes but you can use simple objects as shown above.

"""
}
