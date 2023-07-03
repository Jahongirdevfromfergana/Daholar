package uz.fergana.daholar.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import uz.fergana.daholar.R
import uz.fergana.daholar.adapters.AdibAdapter
import uz.fergana.daholar.databinding.FragmentSearchBinding
import uz.fergana.daholar.library.SmoothBottomBar
import uz.fergana.daholar.models.Adib


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SearchFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var binding: FragmentSearchBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var list: ArrayList<Adib>
    private lateinit var adibAdapter: AdibAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.apply {
            (requireActivity() as AppCompatActivity).findViewById<SmoothBottomBar>(R.id.bottomBar).visibility =
                View.GONE
            firestore = Firebase.firestore
            list = ArrayList()
            loadData()
        }
        return binding.root
    }

    private fun loadData() {
        firestore.collection("adib")
            .get()
            .addOnSuccessListener { querySnapshot ->
                list.clear()
                querySnapshot.forEach { queryDocumentSnapshot ->
                    val adib = queryDocumentSnapshot.toObject(Adib::class.java)
                    adib.uid = queryDocumentSnapshot.id
                    list.add(adib)
                    if (list.isNotEmpty()) {
                        binding.lottie.visibility = View.INVISIBLE
                    } else {
                        binding.lottie.visibility = View.VISIBLE
                    }
                    adibAdapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }

        adibAdapter = AdibAdapter(
            requireContext(),
            list,
            object : AdibAdapter.OnItemClickListener {
                override fun onItemClickListener(adib: Adib, position: Int) {
                    val bundle = Bundle()
                    bundle.putSerializable("adib", adib)
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.aboutAdibFragment, bundle)
                    binding.search.setText("")
                }

                override fun onItemSaveListener(
                    adib: Adib,
                    position: Int,
                    holder: AdibAdapter.Vh
                ) {
                    if (adib.isSave == true) {
                        holder.itemAdibBinding.save.setBackgroundResource(R.drawable.save2)
                        holder.itemAdibBinding.saveImg.setImageResource(R.drawable.ic_saqlangan)
                        adib.isSave = false
                        firestore.collection("adib").document(adib.uid.toString()).set(adib)
                    } else {
                        holder.itemAdibBinding.save.setBackgroundResource(R.drawable.save1)
                        holder.itemAdibBinding.saveImg.setImageResource(R.drawable.ic_saqlanganlar)
                        adib.isSave = true
                        firestore.collection("adib").document(adib.uid.toString()).set(adib)
                    }
                }
            })

        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                val text = p0.toString()
                if (text.isNotEmpty()) {
                    binding.clearText.visibility = View.VISIBLE
                } else {
                    binding.clearText.visibility = View.GONE
                }
                filter(text)
            }
        })
        binding.clearText.setOnClickListener {
            binding.search.setText("")
        }
        binding.rv.adapter = adibAdapter
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun filter(text: String) {
        val filteredList = ArrayList<Adib>()
        for (passport in list) {
            if (passport.name?.lowercase()!!.contains(text.lowercase())
                || (passport.birthYear)!!.lowercase().contains(text.lowercase())
                || (passport.diedYear)!!.lowercase().contains(text.lowercase())
            ) {
                filteredList.add(passport)
            }
        }
        adibAdapter.filterList(filteredList)
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }
}