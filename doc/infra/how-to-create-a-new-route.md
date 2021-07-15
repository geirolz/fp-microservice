##How to create a new route
Steps
1. Create route endpoint definition 
2. Create Route implementation
3. Add route to service routes

### 1. Create route endpoint definition

The package `[...].infra.route.endpoint` contains only the endpoint api definition and the contract model.
So, to create a new endpoint definition we have to:
- Create a new package under `endponts` named with the context of your endpoint (eg. user)
- Create a new package named `contract` (not plural) under the new `[Context]` package, this package will contains all the contracts model with their mapper (eg. UserContract)
- Create a new `singleton` object named as `[Context]EndpointApi` (eg. UserEndpointApi) private for the package `route` under the new `[Context]` package you have to. 
This object will contain all endpoints definition for specified context.
  
  
```
.
└── [...]infra.route.endpoint
    └── [CONTEXT]
      ├── contract
      | └──  [... all models ...]
      └── [CONTEXT]EndpointApi.scala
```


#### [Context]EndpointApi

Once create out object should be like this:
```scala
private[route] object UserEndpointApi {}
```

Let's define our `private` common endpoint for our context
```scala
private val user: Endpoint[Unit, Unit, Unit, Any] =
    VersionedEndpoint.v1.in("user")
```

Once done we can define our real endpoint, in this example we define an endpoint to get user information by Id

_UserEndpointApi.scala_
```scala

  //new type placed in domain model package
  case class UserId(value: Long) extends AnyVal
  
  
  val getById: Endpoint[UserId, ErrorInfo, UserContract, Any] =
    user.get
      .in(query[UserId]("id"))
      .out(jsonBody[UserContract])
      .errorOut(jsonBody[ErrorInfo])
```

In our `contract` package we have our models, both private for `route` package because we don't want use these class outside that package
```scala
private[route] case class UserContract(id: Long, name: String, surname: String)

private[route] sealed trait UserEndpointError
private[route] object UserEndpointError {
  case class UserNotFound(userId: UserId) extends UserEndpointError
}
```

### 2. Create Route implementation
TODO

### 3. Add route to service routes
TODO