package loki.language.generation.bytecodetemplate

import assembler.builder.MethodBuilder
import loki.language.generation.constant.{BytecodeMethodDescriptors, LanguageMembers}
import loki.runtime.LType

private[generation] object TypeBytecodeTemplate
{
	implicit class TypeBytecodeTemplate(val methodBuilder:MethodBuilder)
	{
		def invokeInitType():methodBuilder.type =
			methodBuilder invokeinit(classOf[LType], BytecodeMethodDescriptors.TYPE__METHOD__INIT)

		def invokestaticTypeMethodCreateAnonymous():methodBuilder.type = methodBuilder invokestatic (
			classOf[LType],
			LanguageMembers.TYPE__METHOD__CREATE_ANONYMOUS,
			BytecodeMethodDescriptors.TYPE__METHOD__CREATE_ANONYMOUS
		)
	}
}
