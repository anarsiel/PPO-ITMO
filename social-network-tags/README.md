# Social-network-tags

Component that calculates the frequency of occurrence of tweets with
a certain hashtag over the past few hours.

### Example

``` kotlin
val service = Service(VK)

val statistics5Hours: Response = service.calcFrequency(
    tag = "abracadabra",
    hours = 5,
)
// SuccessfulResponse(frequency=[1, 1, 2, 2, 0], error=null)

val statistics10Hours: Response = service.calcFrequency(
    tag = "abracadabra",
    hours = 10,
)
// SuccessfulResponse(frequency=[3, 1, 0, 0, 2, 1, 1, 2, 2, 0], error=null)

val statistics239Hours: Response = service.calcFrequency(
    tag = "abracadabra",
    hours = 239,
)
// UnsuccessfulResponse(statistics=null, error=`hours` value must be in range [1 .. 24])
```