base_price = spel{#product.getBasePrice()}
discount = 0

# Apply customer loyalty discount
if spel{#customer.loyaltyYears()} > 2:
    discount += 0.05

# Apply volume discount
if spel{#product.getQuantity()} > 10:
    discount += 0.03

final_price = base_price * (1 - discount)

# Return the calculated price to Java
o4java{final_price}