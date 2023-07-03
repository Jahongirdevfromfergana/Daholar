package uz.fergana.daholar.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import uz.fergana.daholar.R
import uz.fergana.daholar.databinding.FragmentAddBinding
import uz.fergana.daholar.library.SmoothBottomBar
import uz.fergana.daholar.models.Adib

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var binding: FragmentAddBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var reference: StorageReference
    private var imageUrl = ""
    private var type1 = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(inflater, container, false)
        binding.apply {
            (requireActivity() as AppCompatActivity).findViewById<SmoothBottomBar>(R.id.bottomBar).visibility =
                View.GONE
            firestore = Firebase.firestore
            storage = FirebaseStorage.getInstance()
            reference = storage.getReference("photos")

            addImg.setOnClickListener {
                launcher.launch("image/*")
            }

            val adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.type,
                android.R.layout.simple_spinner_item
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            type.adapter = adapter
            type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    type1 = p0?.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }

            add.setOnClickListener {
                if (fullname.text.isNotEmpty() && birth.text.isNotEmpty() && died.text.isNotEmpty() && about.text.isNotEmpty() && imageUrl.isNotEmpty()) {
                    val adib = Adib(
                        System.currentTimeMillis().toString(),
                        fullname.text.toString(),
                        birth.text.toString(),
                        died.text.toString(),
                        type1,
                        about.text.toString(),
                        false,
                        imageUrl
                    )
                    firestore.collection("adib").add(adib).addOnSuccessListener {
                        Navigation.findNavController(root).popBackStack()
                    }.addOnFailureListener { e ->
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Ma'lumotlar to'liq kiritilmagan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        return binding.root
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it == null) return@registerForActivityResult
        reference
            .child("${System.currentTimeMillis()}.png")
            .putFile(it)
            .addOnSuccessListener {
                it.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->
                    imageUrl = uri.toString()
                    Glide.with(requireContext())
                        .load(uri)
                        .apply(
                            RequestOptions().placeholder(
                                R.drawable.img
                            ).centerCrop()
                        )
                        .into(binding.image)
                }
            }.addOnFailureListener {
                Toast.makeText(
                    requireContext(), it.message, Toast.LENGTH_SHORT
                ).show()
            }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}