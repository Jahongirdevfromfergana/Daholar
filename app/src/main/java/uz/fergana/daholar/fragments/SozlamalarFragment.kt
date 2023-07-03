package uz.fergana.daholar.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import uz.fergana.daholar.R
import uz.fergana.daholar.databinding.FragmentSozlamalarBinding
import uz.fergana.daholar.library.SmoothBottomBar

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SozlamalarFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var binding: FragmentSozlamalarBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSozlamalarBinding.inflate(inflater, container, false)
        binding.apply {
            (requireActivity() as AppCompatActivity).findViewById<SmoothBottomBar>(R.id.bottomBar).visibility =
                View.VISIBLE
            share.setOnClickListener {

            }
            info.setOnClickListener {
                Navigation.findNavController(root).navigate(R.id.infoAppFragment)
            }
            add.setOnClickListener {
                Navigation.findNavController(root).navigate(R.id.addFragment)
            }
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SozlamalarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}