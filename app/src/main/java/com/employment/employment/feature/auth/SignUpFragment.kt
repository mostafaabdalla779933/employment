package com.employment.employment.feature.auth

import com.employment.employment.common.base.BaseFragment
import com.employment.employment.databinding.FragmentSignUpBinding
import com.google.android.material.tabs.TabLayoutMediator

class SignUpFragment : BaseFragment<FragmentSignUpBinding>(){

    private lateinit var adapter: PagerAdapter
    override fun initBinding()= FragmentSignUpBinding.inflate(layoutInflater)
    override fun onFragmentCreated() {
        initAdapter()
    }

    private fun initAdapter(){
        binding.apply {
            adapter = PagerAdapter(this@SignUpFragment)
            viewPager.adapter = adapter

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> "Company"
                    else -> "User"
                }
            }.attach()


        }
    }

}