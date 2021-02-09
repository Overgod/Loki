package assembler.builder

import assembler.Utils
import assembler.constant.{InternalDescriptors, Modifier}
import assembler.methoddescriptor.{MethodDescriptor, MethodInvocationDescriptor}
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree._

import scala.collection.mutable

class MethodBuilder private[builder](modifier:Modifier, val name:String, descriptor:String)
{
	private[builder] val methodNode = new MethodNode(modifier.code, name, descriptor, null, null)

	private val objectStackCounter = new ObjectStackCounter(this)

	private[assembler] def this(modifier:Modifier, methodDescriptor:MethodDescriptor) =
		this(modifier, methodDescriptor.name, methodDescriptor.untypedSignature)

	def aconstnull():this.type =
	{
		methodNode.instructions.add(new InsnNode(Opcodes.ACONST_NULL))
		this
	}

	def iconsttrue():this.type =
	{
		methodNode.instructions.add(new InsnNode(Opcodes.ICONST_1))
		this
	}

	def lconst1():this.type =
	{
		methodNode.instructions.add(new InsnNode(Opcodes.LCONST_1))
		this
	}

	/*Push item from run-time constant pool*/
	def ldc(any:Any):this.type =
	{
		methodNode.instructions.add(new LdcInsnNode(any))
		this
	}


	def aload(variableIndex:Int):this.type =
	{
		methodNode.instructions.add(new VarInsnNode(Opcodes.ALOAD, variableIndex))
		this
	}

	def aloadthis():this.type =
	{
		methodNode.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0))
		this
	}

	def aaload():this.type =
	{
		methodNode.instructions.add(new InsnNode(Opcodes.AALOAD))
		this
	}

	def aastore():this.type =
	{
		methodNode.instructions.add(new InsnNode(Opcodes.AASTORE))
		this
	}

	def astore(variableIndex:Int):this.type =
	{
		methodNode.instructions.add(new VarInsnNode(Opcodes.ASTORE, variableIndex))
		this
	}

	def pop():this.type =
	{
		methodNode.instructions.add(new InsnNode(Opcodes.POP))
		this
	}

	def dup():this.type =
	{
		methodNode.instructions.add(new InsnNode(Opcodes.DUP))
		this
	}

	def swap():this.type =
	{
		methodNode.instructions.add(new InsnNode(Opcodes.SWAP))
		this
	}

	def ifeq(labelNode:LabelNode):this.type =
	{
		methodNode.instructions.add(new JumpInsnNode(Opcodes.IFEQ, labelNode))
		this
	}

	def ifne(labelNode:LabelNode):this.type =
	{
		methodNode.instructions.add(new JumpInsnNode(Opcodes.IFNE, labelNode))
		this
	}

	def goto(labelNode:LabelNode):this.type =
	{
		methodNode.instructions.add(new JumpInsnNode(Opcodes.GOTO, labelNode))
		this
	}

	def label(labelNode:LabelNode):this.type =
	{
		methodNode.instructions.add(labelNode)
		this
	}

	def getfield(ownerClass:Class[_], name:String, `class`:Class[_]):this.type =
		getfield(Utils getClassInternalName ownerClass, name, Utils getClassInternalName `class`)

	def getfield(ownerClass:Class[_], name:String, classInternalName:String):this.type =
		getfield(Utils getClassInternalName ownerClass, name, classInternalName)

	def getfield(ownerClassInternalName:String, name:String, `class`:Class[_]):this.type =
		getfield(ownerClassInternalName, name, Utils getClassInternalName `class`)

	def getfield(ownerClassInternalName:String, name:String, classInternalName:String):this.type =
	{
		methodNode
			.instructions
			.add(
				new FieldInsnNode(
					Opcodes.GETFIELD,
					ownerClassInternalName,
					name,
					Utils getClassDescriptor classInternalName
				)
			)

		this
	}

	def getstatic(ownerClass:Class[_], name:String, `class`:Class[_]):this.type =
		getstatic(Utils getClassInternalName ownerClass, name, Utils getClassInternalName `class`)

	def getstatic(ownerClass:Class[_], name:String, classInternalName:String):this.type =
		getstatic(Utils getClassInternalName ownerClass, name, classInternalName)

	def getstatic(ownerClassInternalName:String, name:String, `class`:Class[_]):this.type =
		getstatic(ownerClassInternalName, name, Utils getClassInternalName `class`)

	def getstatic(ownerClassInternalName:String, name:String, classInternalName:String):this.type =
	{
		methodNode
			.instructions
			.add(
				new FieldInsnNode(
					Opcodes.GETSTATIC, ownerClassInternalName, name, Utils getClassDescriptor classInternalName
				)
			)

		this
	}

	def putfield(ownerClass:Class[_], name:String, `class`:Class[_]):this.type =
		putfield(Utils getClassInternalName ownerClass, name, Utils getClassDescriptor `class`)

	def putfield(ownerClass:Class[_], name:String, descriptor:String):this.type =
		putfield(Utils getClassInternalName ownerClass, name, descriptor)

	def putfield(ownerClassInternalName:String, name:String, `class`:Class[_]):this.type =
		putfield(ownerClassInternalName, name, Utils getClassDescriptor `class`)

	def putfield(ownerClassInternalName:String, name:String, descriptor:String):this.type =
	{
		methodNode.instructions.add(new FieldInsnNode(Opcodes.PUTFIELD, ownerClassInternalName, name, descriptor))
		this
	}

	def invokevirtual(methodInvocationDescriptor:MethodInvocationDescriptor):this.type =
		invokevirtual(
			methodInvocationDescriptor.ownerClassInternalName,
			methodInvocationDescriptor.name,
			methodInvocationDescriptor.untypedSignature,
			isInterfaceMethod = false
		)

	def invokevirtual(
		methodOwnerClassInternalName:String,
		methodName:String,
		methodSignature:String,
		isInterfaceMethod:Boolean
	):this.type =
	{
		methodNode
			.instructions
			.add(
				new MethodInsnNode(
					Opcodes.INVOKEVIRTUAL, methodOwnerClassInternalName, methodName, methodSignature, isInterfaceMethod
				)
			)

		this
	}

	def invokeinit(methodInvocationDescriptor:MethodInvocationDescriptor):this.type =
		invokeinit(methodInvocationDescriptor.ownerClassInternalName, methodInvocationDescriptor.untypedSignature)

	def invokeinit(initMethodOwnerClassInternalName:String, initMethodSignature:String):this.type =
	{
		methodNode
			.instructions
			.add(
				new MethodInsnNode(
					Opcodes.INVOKESPECIAL,
					initMethodOwnerClassInternalName,
					InternalDescriptors.INIT_METHOD,
					initMethodSignature,
					false
				)
			)

		this
	}

	def invokestatic(methodInvocationDescriptor:MethodInvocationDescriptor):this.type =
		invokestatic(
			methodInvocationDescriptor.ownerClassInternalName,
			methodInvocationDescriptor.name,
			methodInvocationDescriptor.untypedSignature
		)

	def invokestatic(methodOwnerClassInternalName:String, methodName:String, methodSignature:String):this.type =
	{
		methodNode
			.instructions
			.add(
				new MethodInsnNode(
					Opcodes.INVOKESTATIC, methodOwnerClassInternalName, methodName, methodSignature, false
				)
			)

		this
	}

	def `new`(classInternalName:String):this.type =
	{
		methodNode.instructions.add(new TypeInsnNode(Opcodes.NEW, classInternalName))
		this
	}

	def `new`(`class`:Class[_]):this.type =
	{
		methodNode.instructions.add(new TypeInsnNode(Opcodes.NEW, Utils getClassInternalName `class`))
		this
	}

	def anewarray(arrayClass:Class[_]):this.type =
	{
		methodNode.instructions.add(new TypeInsnNode(Opcodes.ANEWARRAY, Utils getClassInternalName arrayClass))
		this
	}

	def aReturn():this.type =
	{
		methodNode.instructions.add(new InsnNode(Opcodes.ARETURN))
		this
	}

	def `return`():this.type =
	{
		methodNode.instructions.add(new InsnNode(Opcodes.RETURN))
		this
	}

	def incrementObjectStackCounter():this.type =
	{
		objectStackCounter.increment()
		this
	}

	def decrementObjectStackCounter():this.type =
	{
		objectStackCounter.decrement()
		this
	}

	def increaseObjectStackCounter(count:Int):this.type =
	{
		objectStackCounter.increase(count)
		this
	}

	def decreaseObjectStackCounter(count:Int):this.type =
	{
		objectStackCounter.decrease(count)
		this
	}

	def pushFrame():this.type =
	{
		objectStackCounter.push()
		this
	}

	def popFrame(rest:Int = 0):this.type =
	{
		objectStackCounter.pop(rest)
		this
	}
}

private class ObjectStackCounter(method:MethodBuilder)
{
	private val counters = mutable.Stack[Int](0)

	def increment():Unit = increase(1)
	def decrement():Unit = decrease(1)
	def decrease(count:Int) = counters(0) -= count
	def increase(count:Int) = counters(0) += count

	def push():Unit = counters.push(0)

	def pop(rest:Int):Int =
	{
		for (i <- 0 until counters.head - rest) method.pop()
		counters.pop()
	}
}