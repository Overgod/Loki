package loki.runtime.datatype;


import loki.runtime.builtin.operation.binary.bool.LBooleanAmpersandAmpersand;
import loki.runtime.builtin.operation.binary.bool.LBooleanPipePipe;
import loki.runtime.builtin.operation.unary.bool.LBooleanNegation;
import loki.runtime.constant.LTypes;
import loki.runtime.datatype.unit.LUnit;

public class LBooleanPrototype extends LUnit
{
	public static final LBooleanPrototype instance = new LBooleanPrototype();

	private boolean value;

	protected LBooleanPrototype(boolean value)
	{
		super(LTypes.BOOLEAN);
		this.value = value;
	}

	private LBooleanPrototype()
	{
		super(LTypes.BOOLEAN_PROTOTYPE);
		initializeBuiltins();
	}

	public boolean getValue()
	{
		return value;
	}

	@Override
	public int _hashCode()
	{
		return Boolean.hashCode(value);
	}

	@Override
	public boolean _equals(LUnit unit)
	{
		LBoolean boolean_ = unit.asType(LTypes.BOOLEAN);
		return boolean_ != null && getValue() == boolean_.getValue();
	}

	private void initializeBuiltins()
	{
		LBooleanNegation.instance.init(this);
		LBooleanAmpersandAmpersand.instance.init(this);
		LBooleanPipePipe.instance.init(this);
	}
}
