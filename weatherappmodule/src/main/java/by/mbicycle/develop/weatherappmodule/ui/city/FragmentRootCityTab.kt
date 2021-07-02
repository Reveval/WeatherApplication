package by.mbicycle.develop.weatherappmodule.ui.city

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import by.mbicycle.develop.weatherappmodule.R
import by.mbicycle.develop.weatherappmodule.databinding.RootCityTabFragmentBinding

class FragmentRootCityTab : Fragment() {
    lateinit var binding: RootCityTabFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RootCityTabFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigateToCityFragment()
    }

    private fun navigateToCityFragment() {
        childFragmentManager.commit {
            replace<CityFragment>(R.id.root_city_fragment_container)
        }
    }
}