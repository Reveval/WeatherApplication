package by.mbicycle.develop.weatherappmodule.ui.city

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import by.mbicycle.develop.weatherappmodule.R
import by.mbicycle.develop.weatherappmodule.UpdateDataListener
import by.mbicycle.develop.weatherappmodule.databinding.FragmentRootCityTabBinding

class RootCityTabFragment : Fragment(), UpdateDataListener {
    lateinit var binding: FragmentRootCityTabBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRootCityTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.commit {
            add<CityFragment>(R.id.root_city_fragment_container, CityFragment::class.java.simpleName)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

    override fun reloadData() {
        val childFragmentResult = childFragmentManager.findFragmentByTag(CityFragment::class.java.simpleName)
        childFragmentResult?.let {
            if (it is UpdateDataListener) {
                it.reloadData()
            }
        }
    }
}