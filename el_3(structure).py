#add items to a cake
def add_sprinkles(func):
    def wrapper(*args):
        print("*You add sprinkles*")
        func(*args)
    return wrapper

def add_fudge(func):
    def wrapper(*args):
        print("*You add fudge*")
        func(*args)
    return wrapper

@add_sprinkles
@add_fudge
def get_ice_cream(flavor):
    print(f"Here is your {flavor} ice cream")

get_ice_cream("vanilla")

print('*************next program**************')
#Adding extra features to a basic coffee.

class Coffee:

    def cost(self):
        return 5
class Milk:
    def __init__(self,coffee):
        self._coffee=coffee

    def cost(self):
        return self._coffee.cost() + 1
class Sugar:
    def __init__(self,coffee):
        self._coffee=coffee

    def cost(self):
        return self._coffee.cost()+0.5

coffee=Coffee()
print(coffee.cost())  
milk_coffee=Milk(coffee)
print(milk_coffee.cost()) 
sugar_milk_coffee=Sugar(milk_coffee)
print(sugar_milk_coffee.cost())