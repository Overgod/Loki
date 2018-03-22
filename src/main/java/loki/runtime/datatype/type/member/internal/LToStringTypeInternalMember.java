package loki.runtime.datatype.type.member.internal;

import loki.runtime.datatype.LString;
import loki.runtime.datatype.LUnit;

public class LToStringTypeInternalMember extends LTypeInternalMember
{
	public static final LToStringTypeInternalMember instance = new LToStringTypeInternalMember();

	@Override
	public LUnit apply(String name, long id, LUnit[] parameters)
	{
		return new LString(name);
	}
}
