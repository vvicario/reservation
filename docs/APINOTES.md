### Reservation API

# POST /reservation

* It receives the following JSON:

  {
        "arrivalDate": "2019-02-16",
        "departureDate": "2019-02-17",
        "email":"test@test.com",
        "fullName": "Veronica Vicario"
  }

It returns a reservation identifier in the location header
# GET reservation/available
It has two optionals parameters, from and to, and must be send with the followign format: yyyy-MM-dd
Example:

* reservation/available?from=2019-02-16&to=2019-02-28

# PUT /reservation/:identifier
It receives the following JSON:

{ "arrivalDate": "2019-02-16", "departureDate": "2019-02-17", "email":"test@test.com", "fullName": "Veronica Vicario" }

It returns the reservation with values updated
# DELETE /reservation/:identifier
# GET reservation/:identifier
