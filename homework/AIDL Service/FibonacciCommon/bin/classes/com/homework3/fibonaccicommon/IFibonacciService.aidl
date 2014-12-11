package com.homework3.fibonaccicommon;

import com.homework3.fibonaccicommon.FibonacciRequest;
import com.homework3.fibonaccicommon.FibonacciResponse;

interface IFibonacciService {
    long fibJR(in long n);
    long fibJI(in long n);
    long fibNR(in long n);
    long fibNI(in long n);
    FibonacciResponse fib(in FibonacciRequest request);
}
