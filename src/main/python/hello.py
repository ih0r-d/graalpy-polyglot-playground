import polyglot



class PyHello:
    def hello(self, txt):
        print("Hello, " + txt)

    def num(self):
        return 42

    def sum(self, a, b):
        return a + b

# We export the PyHello class to Java as our explicit interface with the Java side
polyglot.export_value("Hello", PyHello)