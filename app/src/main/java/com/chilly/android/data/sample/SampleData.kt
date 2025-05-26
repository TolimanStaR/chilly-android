package com.chilly.android.data.sample

import com.chilly.android.data.remote.dto.PlaceDto

val place1 = PlaceDto(
    id = 1,
    name = "Sample Place",
    address = "123 Test Street",
    imageUrls = listOf("https://example.com/image1.jpg", "https://example.com/image2.jpg"),
    openHours = listOf("Mon-Fri: 9AM-6PM", "Sat-Sun: 10AM-4PM"),
    phone = "+1234567890",
    rating = 4.5f,
    socials = listOf("https://instagram.com/sampleplace", "https://facebook.com/sampleplace"),
    website = "https://sampleplace.com",
    yandexMapsLink = "https://yandex.com/maps/org/123456789",
    latitude = 55.751244,
    longitude = 37.618423
)

val place2 = PlaceDto(
    id = 2,
    name = "Another Place",
    address = "456 Test Avenue",
    imageUrls = listOf("https://example.com/image3.jpg"),
    openHours = listOf("Daily: 24 hours"),
    phone = "+0987654321",
    rating = 3.8f,
    socials = listOf("https://twitter.com/anotherplace"),
    website = "https://anotherplace.com",
    yandexMapsLink = "https://yandex.com/maps/org/987654321",
    latitude = 55.755826,
    longitude = 37.617300
)

val placeList = listOf(place1, place2)