package cloki.runtime.datatype;

import cloki.runtime.builtins.LBuiltins;
import cloki.util.IdGenerator;

public class LType extends LUnit
{
	private static volatile LType type;
	private static final IdGenerator idGenerator = new IdGenerator();

	public final long id = idGenerator.apply();

	private final String name;
	private volatile boolean builtinsInitialized = false;

	public LType(LType original)
	{
		this(original.getName());
	}

	public LType(String name)
	{
		super(null);
		this.name = name;
	}

	public static LType createAnonymous()
	{
		return new LType(cloki.runtime.constant.LType.ANONYMOUS.name);
	}

	public String getName()
	{
		return name;
	}

	public String getNameWithId()
	{
		return String.format("%s#%s", name, id);
	}

	@Override
	public LType getType()
	{
		if (type == null) synchronized(guard)
		{
			type = new LType(cloki.runtime.constant.LType.TYPE.name);
		}

		return type;
	}

	@Override
	public LUnit getMember(String memberName)
	{
		if (!builtinsInitialized) synchronized(guard)
		{
			if (!builtinsInitialized)
			{
				LBuiltins.initType(this);
				builtinsInitialized = true;
			}
		}

		return super.getMember(memberName);
	}

	@Override
	public boolean _equals(LUnit unit)
	{
		return this == unit;
	}

	@Override
	public String _toString()
	{
		return name;
	}
}
