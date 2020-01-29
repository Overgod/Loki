package loki.runtime.datatype.unit;

import loki.runtime.LSettings;
import loki.runtime.builtin.operation.binary.unit.LUnitBangEquals;
import loki.runtime.builtin.operation.binary.unit.LUnitEqualsEquals;
import loki.runtime.constant.LDataUnit;
import loki.runtime.constant.LTypes;
import loki.runtime.constant.LUnitMember;
import loki.runtime.context.LUnitContext;
import loki.runtime.datatype.*;
import loki.runtime.datatype.number.LNumber;
import loki.runtime.datatype.type.LType;
import loki.runtime.datatype.unit.member.*;
import loki.runtime.util.Compiler;
import loki.runtime.util.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

import static loki.runtime.util.Polymorphic.Type.ACCESS;
import static loki.runtime.util.Polymorphic.Type.DEFAULT;

public abstract class LUnit
{
	public static final LUnit[] EMPTY_UNIT_ARRAY = {};

	private static volatile LUnit prototype;

	private final LType type;
	private volatile @Nullable LUnitContext capturedOnCreationUnitContext;
	private volatile @Nullable ConcurrentLinkedDeque<LUnit> parents;
	private volatile @Nullable ConcurrentMap<String, LUnit> members;
	private volatile @Nullable Map<String, Integer> parameterIndexes;

	public LUnit(@Nullable LType type)
	{
		this(type, null);
	}

	public LUnit(@Nullable LType type, @Nullable LUnitContext capturedOnCreationUnitContext)
	{
		this.type = type;
		this.capturedOnCreationUnitContext = capturedOnCreationUnitContext;
	}

	@Internal
	public LUnit instantiate(@Nullable LUnit[] parameters, @Nullable Consumer<LUnit> saver)
	{
		LUnit self = this;

		LUnit newUnit = new LUnit(new LType(getType().getName()), getCapturedOnCreationUnitContext())
		{
			@Override
			public LUnit call(LUnit host, @Nullable LUnit[] parameters)
			{
				return self.call(host, parameters);
			}
		};

		if (saver != null) saver.accept(newUnit);

		newUnit.addParent(this);

		call(newUnit, parameters);

		return newUnit;
	}

	@Internal
	public static LUnit getPrototype()
	{
		initPrototypeIfNecessary();

		return prototype;
	}

	@Internal
	public LType getType()
	{
		return type;
	}

	@Internal
	public @Nullable Map<String, Integer> getParameterIndexes()
	{
		return parameterIndexes;
	}

	@Compiler
	protected @Nullable LUnitContext getCapturedOnCreationUnitContext()
	{
		return capturedOnCreationUnitContext;
	}

	@Compiler
	public LUnit getMember(String memberName)
	{
		if (members != null)
		{
			LUnit member = members.get(memberName);

			if (member != null) return member;
		}

		return getSuperMember(memberName);
	}

	@Compiler
	public LUnit setMember(String memberName, LUnit member)
	{
		initMembersIfNecessary().put(memberName, member);

		return member;
	}

	@Compiler
	public LUnit getSuperMember(String superMemberName)
	{
		for (Iterator<LUnit> parentIterator = initParentsIfNecessary().descendingIterator(); parentIterator.hasNext();)
		{
			LUnit parent = parentIterator.next();
			LUnit member = parent.getMember(superMemberName);

			if (member != LUndefined.instance) return member;
		}

		return LUndefined.instance;
	}

	@Internal
	public LUnit addParent(LUnit parent)
	{
		initParentsIfNecessary().add(parent);

		return this;
	}

	@Compiler
	public LUnit call(LUnit host, @Nullable LUnit[] parameters)
	{
		return LUndefined.instance;
	}

	@Compiler
	public LUnit callMember(String memberName, @Nullable LUnit[] parameters)
	{
		return getMember(memberName).call(this, parameters);
	}

	@Internal
	@Polymorphic(DEFAULT)
	public LUnit _getIndexedItem(@Nullable LUnit[] parameters)
	{
		return LUndefined.instance;
	}

	@Internal
	@Polymorphic(DEFAULT)
	public LUnit _setIndexedItem(@Nullable LUnit[] parameters)
	{
		return LUndefined.instance;
	}

	@Compiler
	public LUnit setParameterNames(@Nullable String[] parameterNames)
	{
		HashMap<String, Integer> parameterIndexes =
			new HashMap<>(LSettings.UNIT_PARAMETER_NAMES_INITIAL_CAPACITY, LSettings.UNIT_PARAMETER_NAMES_LOAD_FACTOR);

		for (int i = 0; i < parameterNames.length; i++) parameterIndexes.put(parameterNames[i], i);

		this.parameterIndexes = parameterIndexes;

		return this;
	}

	@Internal
	@Polymorphic(DEFAULT)
	@Override
	public int hashCode()
	{
		LUnit hashCodeAsUnit = callMember(LUnitMember.HASH_CODE.name, null);
		LNumber hashCodeAsNumber = hashCodeAsUnit.asType(LTypes.NUMBER);

		if (hashCodeAsNumber == null) hashCodeAsNumber = hashCodeAsUnit._hashCode();

		return (int)hashCodeAsNumber.value;
	}

	@Internal
	@Polymorphic(DEFAULT)
	public LNumber _hashCode()
	{
		return new LNumber(super.hashCode());
	}

	@Internal
	@Polymorphic(ACCESS)
	@Override
	public boolean equals(@Nullable Object object)
	{
		if (!(object instanceof LUnit)) return false;

		return callMember(LUnitMember.EQUALS.name, new LUnit[] {(LUnit)object}).toBoolean();
	}

	@Internal
	@Polymorphic(DEFAULT)
	public boolean _equals(@Nullable LUnit unit)
	{
		return super.equals(unit);
	}

	@Compiler
	@Polymorphic(ACCESS)
	@Override
	public String toString()
	{
		LUnit stringAsUnit = callMember(LUnitMember.TO_STRING.name, null);
		LString string = stringAsUnit.asType(LTypes.STRING);

		if (string == null) string = stringAsUnit._toString();

		return string.getValue();
	}

	@Internal
	@Polymorphic(DEFAULT)
	public LString _toString()
	{
		return new LString(getType().toString());
	}

	@Compiler
	@Polymorphic(ACCESS)
	public boolean toBoolean()
	{
		LUnit booleanAsUnit = callMember(LUnitMember.TO_BOOLEAN.name, null);
		LBoolean boolean_ = booleanAsUnit.asType(LTypes.BOOLEAN);

		if (boolean_ == null) boolean_ = booleanAsUnit._toBoolean();

		return boolean_.getValue();
	}

	@Internal
	@Polymorphic(DEFAULT)
	public LBoolean _toBoolean()
	{
		LBoolean thisAsBoolean = asType(LTypes.BOOLEAN);

		if (thisAsBoolean != null) return thisAsBoolean;
		else return
			LBoolean.valueOf(
				this != LVoid.instance &&
				this != LNone.instance &&
				this != LUndefined.instance &&
				(asType(LTypes.NUMBER) == null || ((LNumber)asType(LTypes.NUMBER)).value != 0)
			);
	}

	@Internal
	public @Nullable <TYPE extends LUnit> TYPE asType(LType type)
	{
		if (getType() == type) return (TYPE)this;

		for (Iterator<LUnit> parentIterator = initParentsIfNecessary().descendingIterator(); parentIterator.hasNext();)
		{
			LUnit parentAsType = parentIterator.next().asType(type);

			if (parentAsType != null) return (TYPE)parentAsType;
		}

		return null;
	}

	@Internal
	protected LUnit checkCallParameter(@Nullable LUnit[] parameters, int parameterIndex)
	{
		if (parameters != null && parameterIndex >= 0 && parameterIndex < parameters.length)
			return parameters[parameterIndex];

		LErrors.parameterIsMissedForUnit(parameterIndex, this);

		return LUndefined.instance;
	}

	@Internal
	private ConcurrentMap<String, LUnit> initMembersIfNecessary()
	{
		if (members == null) synchronized(this)
		{
			if (members == null)
				members =
					new ConcurrentHashMap<>(
						LSettings.UNIT_MEMBERS_INITIAL_CAPACITY,
						LSettings.UNIT_MEMBERS_LOAD_FACTOR,
						LSettings.UNIT_MEMBERS_CONCURRENCY_LEVEL
					);
		}

		return members;
	}

	@Internal
	protected ConcurrentLinkedDeque<LUnit> initParentsIfNecessary()
	{
		if (parents == null) synchronized(this)
		{
			if (parents == null)
			{
				parents = new ConcurrentLinkedDeque<>();
				initPrototypeIfNecessary();
				addParent(prototype);
			}
		}

		return parents;
	}

	@Internal
	private static void initPrototypeIfNecessary()
	{
		if (prototype == null) synchronized(LUnit.class)
		{
			if (prototype == null)
				prototype = new LUnit(new LType(LDataUnit.UNIT_PROTOTYPE.name))
				{
					{
						initializeBuiltins();
					}

					@Compiler
					@Override
					public LUnit getSuperMember(String superMemberName)
					{
						return LUndefined.instance;
					}

					@Compiler
					@Override
					public LUnit addParent(LUnit parent)
					{
						LErrors.printErrorAndExit("Unit prototype cannot have parents");

						return LUndefined.instance;
					}

					@Internal
					@Override
					public @Nullable <TYPE extends LUnit> TYPE asType(LType type)
					{
						if (getType().equals(type)) return (TYPE)this;

						return null;
					}

					@Internal
					@Override
					protected @Nullable ConcurrentLinkedDeque<LUnit> initParentsIfNecessary()
					{
						return null;
					}

					@Internal
					private void initializeBuiltins()
					{
						LNew.instance.init(this);
						LAddParent.instance.init(this);
						LGetIndexItem.instance.init(this);
						LSetIndexItem.instance.init(this);
						LGetType.instance.init(this);
						LToString.instance.init(this);
						LToBoolean.instance.init(this);
						LHashCode.instance.init(this);
						LEquals.instance.init(this);
						LUnitEqualsEquals.instance.init(this);
						LUnitBangEquals.instance.init(this);
					}
				};
		}
	}
}