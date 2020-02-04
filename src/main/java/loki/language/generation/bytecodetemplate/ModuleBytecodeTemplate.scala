package loki.language.generation.bytecodetemplate

import assembler.builder.MethodBuilder
import loki.language.generation.constant.{BytecodeLocalVariablesOrParameters, BytecodeMethodDescriptors}
import loki.runtime.unit.LModule

private[generation] object ModuleBytecodeTemplate
{
	implicit class ModuleBytecodeTemplate(val methodBuilder:MethodBuilder)
	{
		def aloadModuleHeirMethodInitParameterUnitContext():methodBuilder.type = (
			methodBuilder
			aload BytecodeLocalVariablesOrParameters.MODULE_HEIR__METHOD__INIT__PARAMETER__UNIT_CONTEXT
		)

		def invokeInitModule():methodBuilder.type = methodBuilder invokeinit (
			classOf[LModule], BytecodeMethodDescriptors.MODULE__METHOD__INIT
		)
	}
}
