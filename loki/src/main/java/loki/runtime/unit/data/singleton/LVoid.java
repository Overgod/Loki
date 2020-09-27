package loki.runtime.unit.data.singleton;

import loki.runtime.marker.compilerapi.common.VoidGetInstance;
import loki.runtime.unit.data.bool.LBoolean;
import loki.runtime.unit.data.number.LNumber;
import loki.runtime.unit.unit.LUnit;
import loki.runtime.unit.unit.member.method.LAddParents;
import loki.runtime.unit.unit.member.method.LHashCode;
import loki.runtime.unit.unit.member.operation.binary.LEquality;
import loki.runtime.unit.unit.member.operation.binary.LInequality;
import loki.runtime.unitdescriptor.LInstanceDescriptor;

import static loki.runtime.error.LErrors.unitHasNoMember;

public class LVoid extends LUnit
{
	public static final LInstanceDescriptor<LVoid> DESCRIPTOR =
		new LInstanceDescriptor<>("void", LVoid.class, LVoid::new);

	private LVoid()
	{
		super(DESCRIPTOR.getUnitType());
	}

	@VoidGetInstance
	public static LVoid getInstance()
	{
		return DESCRIPTOR.getInstance();
	}

	public static boolean hasInstance(LUnit unit)
	{
		return unit == DESCRIPTOR.getInstance();
	}

	@Override
	public LUnit newInstance(LUnit[] parameters)
	{
		return unitHasNoMember(this, "newInstance");
	}

	@Override
	public LUnit getMember(String memberName)
	{
		return unitHasNoMember(this, "getMember");
	}

	@Override
	public LUnit setMember(String memberName, LUnit member)
	{
		return unitHasNoMember(this, "setMember");
	}

	@Override
	public LUnit getSuperMember(String superMemberName)
	{
		return unitHasNoMember(this, "getSuperMember");
	}

	@Override
	public LUnit addParents(LUnit... parents)
	{
		return unitHasNoMember(this, LAddParents.DESCRIPTOR);
	}

	@Override
	public LUnit call(LUnit host, LUnit... parameters)
	{
		return unitHasNoMember(this, "call");
	}

	@Override
	public LUnit callMember(String memberName, LUnit... parameters)
	{
		if (LEquality.DESCRIPTOR.hasUnitName(memberName))
			return LBoolean.valueOf(hasInstance(getParameter(parameters, 0)));

		if (LInequality.DESCRIPTOR.hasUnitName(memberName))
			return LBoolean.valueOf(!hasInstance(getParameter(parameters, 0)));

		if (LHashCode.DESCRIPTOR.hasUnitName(memberName)) return new LNumber(hashCode());

		return unitHasNoMember(this, "callMember");
	}

	@Override
	public int hashCode()
	{
		return 0;
	}

	@Override
	public boolean equals(Object object)
	{
		return DESCRIPTOR.getInstance() == object;
	}

	@Override
	public String toString()
	{
		return DESCRIPTOR.getUnitName();
	}
}