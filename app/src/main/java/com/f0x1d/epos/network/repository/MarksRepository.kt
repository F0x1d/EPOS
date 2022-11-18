package com.f0x1d.epos.network.repository

import com.f0x1d.epos.EposApplication
import com.f0x1d.epos.network.allMarksUrl
import com.f0x1d.epos.network.makeSignedRequest
import com.f0x1d.epos.network.model.response.AllMarksResponse
import com.f0x1d.epos.network.repository.base.BaseRepository
import com.f0x1d.epos.utils.store.OkHttpClientStore
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MarksRepository: BaseRepository<List<AllMarksResponse>>() {

    override suspend fun requestData(): List<AllMarksResponse> = withContext(Dispatchers.IO) {
        val response = OkHttpClientStore.requireClient().makeSignedRequest(
            allMarksUrl(
                EposApplication.appPreferences.askProfileId(),
                EposApplication.appPreferences.askAid()
            ), object : TypeToken<List<AllMarksResponse>>() {}
        ) ?: emptyList()

        return@withContext response.filter {
            it.periods.isNotEmpty()
        }
    }

}