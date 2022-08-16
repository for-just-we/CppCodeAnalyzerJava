#include <inttypes.h>
#include "std_testcase.h"


#ifndef OMITBAD

void bad()
{
    int64_t data;
    int64_t &dataRef = data;
    data = 0LL;
    /* POTENTIAL FLAW: Use a value input from the console */
    fscanf (stdin, "%" SCNd64, &data);
    {
        int64_t data = dataRef;
        {
            /* POTENTIAL FLAW: Adding 1 to data could cause an overflow */
            int64_t result = data + 1;
            printLongLongLine(result);
        }
    }
}

#endif /* OMITBAD */

#ifndef OMITGOOD

/* goodG2B() uses the GoodSource with the BadSink */
static void goodG2B()
{
    int64_t data;
    int64_t &dataRef = data;
    data = 0LL;
    /* FIX: Use a small, non-zero value that will not cause an overflow in the sinks */
    data = 2;
    {
        int64_t data = dataRef;
        {
            /* POTENTIAL FLAW: Adding 1 to data could cause an overflow */
            int64_t result = data + 1;
            printLongLongLine(result);
        }
    }
}

/* goodB2G() uses the BadSource with the GoodSink */
static void goodB2G()
{
    int64_t data;
    int64_t &dataRef = data;
    data = 0LL;
    /* POTENTIAL FLAW: Use a value input from the console */
    fscanf (stdin, "%" SCNd64, &data);
    {
        int64_t data = dataRef;
        /* FIX: Add a check to prevent an overflow from occurring */
        if (data < LLONG_MAX)
        {
            int64_t result = data + 1;
            printLongLongLine(result);
        }
        else
        {
            printLine("data value is too large to perform arithmetic safely.");
        }
    }
}

void good()
{
    goodG2B();
    goodB2G();
}

#endif /* OMITGOOD */


/* Below is the main(). It is only used when building this testcase on
   its own for testing or for building a binary to use in testing binary
   analysis tools. It is not used when compiling all the testcases as one
   application, which is how source code analysis tools are tested. */
#ifdef INCLUDEMAIN

using namespace CWE190_Integer_Overflow__int64_t_fscanf_add_33; /* so that we can use good and bad easily */

int main(int argc, char * argv[])
{
    /* seed randomness */
    srand( (unsigned)time(NULL) );
#ifndef OMITGOOD
    printLine("Calling good()...");
    good();
    printLine("Finished good()");
#endif /* OMITGOOD */
#ifndef OMITBAD
    printLine("Calling bad()...");
    bad();
    printLine("Finished bad()");
#endif /* OMITBAD */
    return 0;
}