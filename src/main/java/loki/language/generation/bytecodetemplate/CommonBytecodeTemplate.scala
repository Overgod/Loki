package loki.language.generation.bytecodetemplate

import assembler.builder.MethodBuilder
import loki.runtime.context.{LModuleContext, LUnitContext}
import loki.runtime.unit._
import loki.runtime.unit.`type`.LType
import loki.runtime.unit.number.LNumber
import loki.runtime.unit.unit.LUnit

private[generation] object CommonBytecodeTemplate
{
	implicit class CommonBytecodeTemplate(val methodBuilder:MethodBuilder)
	{
		def anewarrayJavaString():methodBuilder.type = methodBuilder anewarray classOf[String]

		def newUnitContext():methodBuilder.type = methodBuilder `new` classOf[LUnitContext]

		def newModuleContext():methodBuilder.type = methodBuilder `new` classOf[LModuleContext]

		def newType():methodBuilder.type = methodBuilder `new` classOf[LType]

		def newNumber():methodBuilder.type = methodBuilder `new` classOf[LNumber]

		def newString():methodBuilder.type = methodBuilder `new` classOf[LString]

		def newArray():methodBuilder.type = methodBuilder `new` classOf[LArray]

		def newMap():methodBuilder.type = methodBuilder `new` classOf[LMap]

		def newObject():methodBuilder.type = methodBuilder `new` classOf[LObject]

		def anewarrayUnit(arraySize:Int):methodBuilder.type = (
			methodBuilder
				ldc arraySize
				anewarray classOf[LUnit]
		)

		def anewarrayUnit():methodBuilder.type = methodBuilder anewarray classOf[LUnit]

		//TODO: move instance to constants
		def void():methodBuilder.type = methodBuilder getstatic (classOf[LVoid], "INSTANCE", classOf[LVoid])
	}
}
