package loki.runtime.builtin.member.unit;

import loki.runtime.builtin.member.LMember;
import loki.runtime.constant.LUnitMember;
import loki.runtime.context.LUnitContext;
import loki.runtime.datatype.LUnit;
import loki.runtime.datatype.type.LType;
import loki.runtime.util.Nullable;

public class LSetIndexItem extends LMember
{
	public static final LSetIndexItem instance = new LSetIndexItem();

	private LSetIndexItem()
	{
		super(new LType(LUnitMember.SET_INDEX_ITEM.name));
	}

	@Override
	public LUnit call(LUnit host, @Nullable LUnit[] parameters, @Nullable LUnitContext unitContext)
	{
		return host._setIndexedItem(parameters);
	}
}
