import json

rows = 12
seat_nrs = 12
seat_price = 18.99
double_seat_price = 25.99

seats = []
for row in range(1, rows + 1):
    prev_double = False
    for seat_nr in range(1, seat_nrs + 1):
        if prev_double:
            prev_double = False
            continue
        
        seat = {
            "row": row,
            "seatNr": seat_nr,
            "price": seat_price
        }
        if row % 2 and seat_nr % 4 == 0 and seat_nr < seat_nrs:
            seat["double_seat"] = True
            seat["price"] = double_seat_price
            prev_double = True
        seats.append(seat)

cinema_hall = {
    "rows": rows,
    "seatNrs": seat_nrs,
    "seats": seats
}

data = {"cinema_hall": cinema_hall}

# Save data to JSON file
file_name = 'cinema_hall_' + str(rows) + 'x' + str(seat_nrs) + '.json'
with open(file_name, 'w') as file:
    json.dump(data, file, indent=2)

print('JSON file created successfully')
