#configuration manager where only one instance should exist.
class Manager:
    goto=None

    def __new__(cls):
        if cls.goto is None:
            cls.goto = super(Manager, cls).__new__(cls)
        return cls.goto

config1=Manager()
config2=Manager()

print(config1 is config2)


#shape factory that creates circles and squares.

class Shape:
    def draw(self):
        pass

class Circle(Shape):
    def draw(self):
        print("Circle")

class Square(Shape):
    def draw(self):
        print("Square")

class ShapeFactory:
    @staticmethod
    def create_shape(shape_type):
        if shape_type=="circle":
            return Circle()
        elif shape_type=="square":
            return Square()

shape1=ShapeFactory.create_shape("circle")
shape1.draw()  

shape2=ShapeFactory.create_shape("square")
shape2.draw()  