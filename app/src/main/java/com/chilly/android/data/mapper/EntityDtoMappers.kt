package com.chilly.android.data.mapper

import com.chilly.android.data.local.entity.EntryWithPlace
import com.chilly.android.data.local.entity.PlaceEntity
import com.chilly.android.data.remote.dto.PlaceDto
import com.chilly.android.domain.model.HistoryItem
import javax.inject.Inject


class PlaceMapper @Inject constructor() {

    fun toDto(entity: PlaceEntity): PlaceDto = PlaceDto(
        id = entity.id,
        address = entity.address,
        imageUrls = entity.imageUrls,
        phone = entity.phone,
        name = entity.name,
        openHours = entity.openHours,
        rating = entity.rating,
        socials = entity.socials,
        website = entity.website,
        yandexMapsLink = entity.yandexMapsLink
    )

    fun toEntity(dto: PlaceDto): PlaceEntity = PlaceEntity(
        id = dto.id,
        address = dto.address,
        imageUrls = dto.imageUrls,
        phone = dto.phone,
        name = dto.name,
        openHours = dto.openHours,
        rating = dto.rating,
        socials = dto.socials,
        website = dto.website,
        yandexMapsLink = dto.yandexMapsLink
    )
}


class HistoryMapper @Inject constructor(
    private val placeMapper: PlaceMapper
) {

    fun toModel(entity: EntryWithPlace): HistoryItem = HistoryItem(
        place = placeMapper.toDto(entity.place),
        timestamp = entity.entry.timestamp
    )
}