/*     ____ ____  ____ ____  ______                                     *\
**    / __// __ \/ __// __ \/ ____/    SOcos COmpiles Scala             **
**  __\_ \/ /_/ / /__/ /_/ /\_ \       (c) 2002, LAMP/EPFL              **
** /_____/\____/\___/\____/____/                                        **
**                                                                      **
** $Id$
\*                                                                      */

package scalac.symtab.classfile;

import scalac.symtab.Type;
import scalac.util.Name;

public interface JavaTypeFactory {
    Type byteType();
    Type shortType();
    Type charType();
    Type intType();
    Type longType();
    Type floatType();
    Type doubleType();
    Type booleanType();
    Type voidType();
    Type classType(Name classname);
    Type arrayType(Type elemtpe);
    Type methodType(Type[] argtpes, Type restpe, Type[] thrown);
    Type packageType(Name packagename);
}
