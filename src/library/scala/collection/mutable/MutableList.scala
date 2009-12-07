/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2003-2010, LAMP/EPFL             **
**  __\ \/ /__/ __ |/ /__/ __ |    http://scala-lang.org/               **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */

// $Id$


package scala.collection
package mutable

import generic._
import immutable.{List, Nil}

/** <p>
 *    This class is used internally to represent mutable lists. It is the
 *     basis for the implementation of the classes
 *     <code>Stack</code>, and <code>Queue</code>.
 *  </p>
 *  !!! todo: convert to LinkedListBuffer?
 *
 *  @author  Matthias Zenger
 *  @author  Martin Odersky
 *  @version 2.8
 *  @since   1
 */
@serializable @SerialVersionUID(5938451523372603072L)
class MutableList[A] extends LinearSeq[A]
                        with LinearSeqLike[A, MutableList[A]]
                        with Builder[A, MutableList[A]] {

  override protected[this] def newBuilder = new MutableList[A]

  protected var first0: LinkedList[A] = new LinkedList[A]
  protected var last0: LinkedList[A] = _ // undefined if first0.isEmpty 
  protected var len: Int = 0

  /** Is the list empty?
   */
  override def isEmpty = len == 0

  /** Returns the first element in this list
   */
  override def head: A = first0.head

  /** Returns the rest of this list
   */
  override def tail: MutableList[A] = {
    require(nonEmpty, "tail of empty list")
    val tl = new MutableList[A]
    tl.first0 = first0.tail
    tl.last0 = last0
    tl.len = len - 1
    tl
  }

  /** Returns the length of this list.
   */
  override def length: Int = len

  /** Returns the <code>n</code>th element of this list.
   *  @throws IndexOutofBoundsException if index does not exist.
   */
  override def apply(n: Int): A = first0.apply(n)

  /** Updates the <code>n</code>th element of this list to a new value.
   *  @throws IndexOutofBoundsException if index does not exist.
   */
  def update(n: Int, x: A): Unit = first0.update(n, x)

  /** Returns the <code>n</code>th element of this list or <code>None</code>
   *  if index does not exist.
   */
  def get(n: Int): Option[A] = first0.get(n)

  protected def prependElem(elem: A) {
    first0 = new LinkedList[A](elem, first0)
    if (len == 0) last0 = first0
    len = len + 1
  }

  protected def appendElem(elem: A): Unit =
    if (len == 0) {
      prependElem(elem)
    } else {
      last0.next = new LinkedList[A]
      last0 = last0.next
      last0.elem = elem
      last0.next = new LinkedList[A] // for performance, use sentinel `object' instead?
      len = len + 1
    }

  @deprecated("use clear instead")
  def reset() { clear() }

  /** Returns an iterator over all elements of this list.
   */
  override def iterator: Iterator[A] = first0.iterator

  override def last = {
    if (isEmpty) throw new NoSuchElementException("MutableList.empty.last")
    last0.elem
  }

  /** Returns an instance of <code>scala.List</code> containing the same
   *  sequence of elements.
   */
  override def toList: List[A] = first0.toList

  /** Returns the current list of elements as a linked List
   *  sequence of elements.
   */
  private[mutable] def toLinkedList: LinkedList[A] = first0

  /** Appends a single element to this buffer. This takes constant time.
   *
   *  @param elem  the element to append.
   */
  def +=(elem: A): this.type = { appendElem(elem); this }

  def clear() {
    first0 = new LinkedList[A]
    last0 = first0
    len = 0
  }

  def result = this
}
