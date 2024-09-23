# simple notification system where multiple users get notified when a message is sent.
# behavioural patter
class Subject:
    def __init__(self):
        self.users=[]

    def attach(self,user):
        self.users.append(user)

    def notify(self,message):
        for user in self.users:
            user.update(message)

class User:
    def update(self, message):
        print(f"User received: {message}")

subject = Subject()
user1 = User()
user2 = User()
subject.attach(user1)
subject.attach(user2)

subject.notify("Hello, World!")

#simple calculator that can switch between addition and subtraction
class behave2:
    def run(self,a,b):
        pass

class Addbehave(behave2):
    def run(self,a,b):
        return a+b

class Subbehave(behave2):
    def run(self,a,b):
        return a-b

class Calculator:
    def __init__(self, beahve):
        self._strategy = beahve

    def set(self, strategy):
        self._strategy = strategy

    def calculate(self, a, b):
        return self._strategy.run(a, b)

calculator = Calculator(Addbehave())
print(calculator.calculate(5,3))  

calculator.set(Subbehave())
print(calculator.calculate(5,3)) 