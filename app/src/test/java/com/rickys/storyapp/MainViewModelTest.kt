package com.rickys.storyapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.rickys.storyapp.api.response.ListStoryItem
import com.rickys.storyapp.data.StoryRepository
import com.rickys.storyapp.ui.mainstory.viewmodel.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var mainViewModel: MainViewModel
    private val dataDummy = DataDummy.generateDummyStories()


    @Before
    fun setUp() {
        mainViewModel = MainViewModel(storyRepository)
    }

    @Test
    fun `when Get Pages Stories Succeed and Should Not Return Null`() = runTest {
        val expectedData: PagingData<ListStoryItem> = PagingData.from(dataDummy)
        val expectedResult = MutableLiveData<PagingData<ListStoryItem>>()
        expectedResult.value = expectedData

        Mockito.`when`(storyRepository.getPagesStories())
            .thenReturn(expectedResult as LiveData<PagingData<ListStoryItem>>)

        val actualStory = mainViewModel.getPagesStories().getOrAwaitValue()

        Mockito.verify(storyRepository).getPagesStories()

        Assert.assertNotNull(actualStory)

        val expectedResultList = expectedData.toList()
        val actualResultList = actualStory.toList()

        Assert.assertEquals(expectedResultList, actualResultList)
        Assert.assertEquals(expectedResultList.size, actualResultList.size)
        Assert.assertEquals(expectedResultList.first(), actualResultList.first())
    }

    @Test
    fun `when Get Pages Empty Should Not Return Data`() = runTest {
        val data: PagingData<ListStoryItem> = PagingData.empty()
        val expectedResult = MutableLiveData<PagingData<ListStoryItem>>()
        expectedResult.value = data

        Mockito.`when`(storyRepository.getPagesStories())
            .thenReturn(expectedResult as LiveData<PagingData<ListStoryItem>>)

        val actualStory = mainViewModel.getPagesStories().getOrAwaitValue()

        Mockito.verify(storyRepository).getPagesStories()

        val actualResultList = actualStory.toList()

        Assert.assertEquals(0, actualResultList.size)
    }
}