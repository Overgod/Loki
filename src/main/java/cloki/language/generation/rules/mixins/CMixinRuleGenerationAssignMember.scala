package cloki.language.generation.rules.mixins

import cloki.language.parsing.CLokiParser.AssignMemberContext

private[language] trait CMixinRuleGenerationAssignMember extends CMixinRuleGeneration[AssignMemberContext]
{
	protected val memberName = ruleContext.IDENTIFIER.getText
	protected val expressionToAssign = ruleContext expression 0
}
