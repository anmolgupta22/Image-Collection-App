
## Image Collection App
This is an Android application that fetches and displays images from the Unsplash API. It allows users to browse and view a collection of high-quality images.
## API Reference

#### Get all items

```http
  GET /photo/
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `api_key` | `string` | **Required**. Your API key |



## Features

- Fetch images from the Unsplash API.
- Display images in a grid layout using RecyclerView.
- Support for paginated loading of images.
- Customize status bar appearance.
- Dependency injection using Hilt.


## Tech Stack

- Kotlin
- Android Jetpack components (ViewModel, LiveData,  RecyclerView)
- Retrofit for network requests
- Dagger Hilt for dependency injection
- Gson for JSON parsing
- Unsplash API for fetching images


## Screenshots

![App Screenshot]
![Screenshot_2024-04-15-02-35-21-063_com example imagecollectionapp](https://github.com/anmolgupta22/Image-Collection-App/assets/92536916/f385c69f-389f-4e2f-bdac-6c70deea9d49)

https://github.com/anmolgupta22/Image-Collection-App/assets/92536916/b5c5f19f-02ff-4cd2-b575-842e9dcb0f38
