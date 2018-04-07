package loki.runtime.datatype.number.member.operation.internal;

import loki.runtime.constant.LBinaryOperator;
import loki.runtime.constant.LType;
import loki.runtime.datatype.LUndefined;
import loki.runtime.datatype.unit.LUnit;
import loki.runtime.datatype.number.LNumber;
import loki.runtime.util.LErrors;

public class LSubtractionNumberInternalOperation extends LNumberInternalOperation
{
	public static final LSubtractionNumberInternalOperation instance = new LSubtractionNumberInternalOperation();

	private LSubtractionNumberInternalOperation()
	{
		super(LBinaryOperator.MINUS.symbol);
	}

	@Override
	public LUnit apply(double value, LUnit[] parameters)
	{
		LNumber parameter = checkRightOperand(parameters);

		if (parameter != null) return new LNumber(value - parameter.value);

		LErrors.printErrorRightOperandDoesNotBelongToTypeOrUndefined(LType.NUMBER.name);
		return LUndefined.instance;
	}
}
