package by.mbicycle.develop.weatherappmodule.ui.city

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import by.mbicycle.develop.weatherappmodule.R
import by.mbicycle.develop.weatherappmodule.databinding.FragmentRootCityTabBinding

class RootCityTabFragment : Fragment() {
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

        navigateToCityFragment()
    }

    private fun navigateToCityFragment() {
        childFragmentManager.commit {
            add<CityFragment>(R.id.root_city_fragment_container)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }
}