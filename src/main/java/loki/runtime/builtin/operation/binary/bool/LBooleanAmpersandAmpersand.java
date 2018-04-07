package loki.runtime.builtin.operation.binary.bool;

import loki.runtime.constant.LBinaryOperator;
import loki.runtime.datatype.LBoolean;
import loki.runtime.datatype.LFalse;
import loki.runtime.datatype.LTrue;
import loki.runtime.datatype.unit.LUnit;

public class LBooleanAmpersandAmpersand extends LBooleanOperation
{
	public static final LBooleanAmpersandAmpersand instance = new LBooleanAmpersandAmpersand();

	private LBooleanAmpersandAmpersand()
	{
		super(LBinaryOperator.AMPERSAND_AMPERSAND);
	}

	@Override
	protected LUnit __execute(LBoolean leftOperand, LBoolean rightOperand)
	{
		return leftOperand.getValue() && rightOperand.getValue() ? LTrue.instance : LFalse.instance;
	}
}
