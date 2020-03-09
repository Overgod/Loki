package loki.runtime.unit.number.member.operation.binary;

import loki.runtime.constant.LBinaryOperator;
import loki.runtime.unit.bool.LBoolean;
import loki.runtime.unit.number.LNumber;
import loki.runtime.LType;
import loki.runtime.unit.unit.LUnit;

public class LLessThanNumberBinaryOperation extends LNumberBinaryOperation
{
	public static final String TYPE_NAME = LBinaryOperator.LESS_THAN.symbol;
	public static final LType TYPE = new LType(TYPE_NAME, LLessThanNumberBinaryOperation.class);

	public static final LLessThanNumberBinaryOperation INSTANCE = new LLessThanNumberBinaryOperation();

	private LLessThanNumberBinaryOperation()
	{
		super(TYPE);
	}

	@Override
	protected LUnit execute(LNumber leftOperand, LNumber rightOperand)
	{
		return LBoolean.valueOf(leftOperand.getValue() < rightOperand.getValue());
	}
}
