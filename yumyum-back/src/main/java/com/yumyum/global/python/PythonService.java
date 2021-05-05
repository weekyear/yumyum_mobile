package com.yumyum.global.python;

import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
//@RequiredArgsConstructor
public class PythonService {

    private static PythonInterpreter interpreter;

    public Object getTest(){
        System.setProperty("python.import.site", "false");

        interpreter = new PythonInterpreter();
        interpreter.execfile("src/main/test.py");
        interpreter.exec("print(jython_predict(5, 10)");

        PyFunction pyFunction = (PyFunction) interpreter.get("jython_predict", PyFunction.class);
        int a = 10;
        int b = 20;
        PyObject obj = pyFunction.__call__(new PyInteger(a), new PyInteger(b));
        System.out.println(obj.toString());

        return obj.toString();
    }
}
