Simple Filter
-------------------------------------
Given a Simple service **A** exposes a rest api **/api/image** and there is a proxy **demo**
which base 64 encodes one field of the http request to service A, test if the http request passing **demo**
is in fact base 64 encoded and the encoded field matches the field give in the request.

Steps:
- run **demo** zuul proxy service
- send http request to **/api/image** ( http request body: **{"image":"test"}** )
- verify **test** value after passing **demo** proxy is base 64 encoded
- verify the encoded value is in fact **test**
- automate the 2 verify steps (eg. in Python)