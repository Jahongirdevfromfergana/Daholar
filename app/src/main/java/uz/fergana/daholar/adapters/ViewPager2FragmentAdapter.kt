package uz.fergana.daholar.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.fergana.daholar.fragments.AdiblarViewPagerFragment

class ViewPager2FragmentAdapter(
    fragment: Fragment,
    private val list: List<String>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return AdiblarViewPagerFragment.newInstance(list[position])
    }
}