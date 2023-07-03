package uz.fergana.daholar.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import uz.fergana.daholar.R
import uz.fergana.daholar.adapters.ViewPager2FragmentAdapter
import uz.fergana.daholar.databinding.CustomTabBinding
import uz.fergana.daholar.databinding.FragmentAdiblarBinding
import uz.fergana.daholar.library.SmoothBottomBar
import kotlin.math.abs

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AdiblarFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var binding: FragmentAdiblarBinding
    private lateinit var viewPager2FragmentAdapter: ViewPager2FragmentAdapter
    val list = arrayOf(
        "Mumtoz",
        "O’zbek",
        "Jahon"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdiblarBinding.inflate(inflater, container, false)
        binding.apply {
            (requireActivity() as AppCompatActivity).findViewById<SmoothBottomBar>(R.id.bottomBar).visibility =
                View.VISIBLE

            appbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
                if (abs(verticalOffset) == appBarLayout?.totalScrollRange) {
                    tabLayout.setBackgroundResource(R.drawable.grey_tablayout_background)
                    toolbar.setBackgroundColor(Color.WHITE)
                } else if (abs(verticalOffset) != appBarLayout?.totalScrollRange) {
                    tabLayout.setBackgroundResource(R.drawable.tablayout_background)
                    toolbar.setBackgroundColor(Color.parseColor("#E5E5E5"))
                }
            }

            searchIcon.setOnClickListener {
                Navigation.findNavController(root).navigate(R.id.searchFragment)
            }
            viewPager2FragmentAdapter =
                ViewPager2FragmentAdapter(this@AdiblarFragment, list.toList())
            binding.viewPager2.adapter = viewPager2FragmentAdapter

            val tabLayoutMediator =
                TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
                    val customTabBinding =
                        CustomTabBinding.inflate(LayoutInflater.from(requireContext()), null, false)
                    customTabBinding.title.text = list[position]
                    if (position == 0) {
                        customTabBinding.liner.setBackgroundResource(R.drawable.tab_selector)
                        customTabBinding.title.setTextColor(Color.WHITE)
                    } else {
                        customTabBinding.liner.setBackgroundResource(R.drawable.tab_unselector)
                        customTabBinding.title.setTextColor(Color.parseColor("#80000000"))
                    }
                    tab.customView = customTabBinding.root
                    tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                        override fun onTabSelected(tab: TabLayout.Tab) {
                            val binding = tab.customView?.let { CustomTabBinding.bind(it) }
                            binding?.liner?.setBackgroundResource(R.drawable.tab_selector)
                            binding?.title?.setTextColor(Color.WHITE)
                        }

                        override fun onTabUnselected(tab: TabLayout.Tab) {
                            val binding = tab.customView?.let { CustomTabBinding.bind(it) }
                            binding?.liner?.setBackgroundResource(R.drawable.tab_unselector)
                            binding?.title?.setTextColor(Color.parseColor("#80000000"))
                        }

                        override fun onTabReselected(tab: TabLayout.Tab?) {

                        }
                    })
                }
            tabLayoutMediator.attach()
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdiblarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}