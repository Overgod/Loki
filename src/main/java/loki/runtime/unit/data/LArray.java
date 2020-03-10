package loki.runtime.unit.data;

import loki.runtime.unit.data.bool.LBoolean;
import loki.runtime.unit.data.number.LNumber;
import loki.runtime.unit.singleton.LVoid;
import loki.runtime.unit.unit.LUnit;
import loki.runtime.unitdescriptor.LDataUnitDescriptor;
import loki.runtime.util.LErrors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringJoiner;

public class LArray extends LUnit
{
	public static final LDataUnitDescriptor<LArray> DESCRIPTOR =
		new LDataUnitDescriptor<>("Array", "ArrayPrototype", LArray.class);

	private final ArrayList<LUnit> items = new ArrayList<>();

	public LArray(LUnit[] items)
	{
		super(DESCRIPTOR.getType());

		_addParents(DESCRIPTOR.getPrototype());

		this.items.addAll(Arrays.asList(items));
	}

	private LArray()
	{
		super(DESCRIPTOR.getPrototypeType());
	}

	@Override
	public LUnit _getIndexedItem(LUnit[] parameters)
	{
		int index = getIndexFromCallParameters(parameters);

		return index >= 0 && index < items.size() ? items.get(index) : LVoid.DESCRIPTOR.getInstance();
	}

	@Override
	public LUnit _setIndexedItem(LUnit[] parameters)
	{
		int index = getIndexFromCallParameters(parameters);

		if (index < 0 || index >= items.size()) LErrors.unitDoesNotHaveItemWithIndex(this, parameters[0]);

		LUnit item = checkCallParameter(parameters, 1);

		items.set(index, item);

		return item;
	}

	@Override
	public LNumber _hashCode()
	{
		return new LNumber(items.hashCode());
	}

	@Override
	public LBoolean _equals(LUnit unit)
	{
		LArray array = unit.asType(DESCRIPTOR.getType());

		return array != null ? LBoolean.valueOf(items.equals(array.items)) : LBoolean.FALSE.getInstance();
	}

	@Override
	public LString _toString()
	{
		if (items.size() == 0) return new LString("[]");

		StringJoiner stringJoiner = new StringJoiner(", ", "[", "]");

		for (LUnit item : items) stringJoiner.add(item.toString());

		return new LString(stringJoiner.toString());
	}

	private int getIndexFromCallParameters(LUnit[] parameters)
	{
		LUnit unitIndex = checkCallParameter(parameters, 0);
		LNumber numberIndex = unitIndex.asType(LNumber.DESCRIPTOR.getType());

		if (numberIndex == null) LErrors.operandShouldHaveType(unitIndex, LNumber.DESCRIPTOR.getType());

		int index = (int)numberIndex.getValue();

		if (index < 0) index = items.size() - Math.abs(index);

		return index;
	}
}
