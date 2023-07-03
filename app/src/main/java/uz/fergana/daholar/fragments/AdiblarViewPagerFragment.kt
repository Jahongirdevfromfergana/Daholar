package uz.fergana.daholar.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import uz.fergana.daholar.R
import uz.fergana.daholar.adapters.AdibAdapter
import uz.fergana.daholar.databinding.FragmentAdiblarViewPagerBinding
import uz.fergana.daholar.models.Adib

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AdiblarViewPagerFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var binding: FragmentAdiblarViewPagerBinding
    private lateinit var list: ArrayList<Adib>
    private lateinit var adibAdapter: AdibAdapter
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdiblarViewPagerBinding.inflate(inflater, container, false)
        binding.apply {
            firestore = Firebase.firestore
            list = ArrayList()

            if (list.isNotEmpty()) {
                binding.lottie.visibility = View.INVISIBLE
            } else {
                binding.lottie.visibility = View.VISIBLE
            }

            when (param1) {
                "Mumtoz" -> {
                    loadData("Mumtoz")
                    adibAdapter = AdibAdapter(
                        requireContext(),
                        list,
                        object : AdibAdapter.OnItemClickListener {
                            override fun onItemClickListener(adib: Adib, position: Int) {
                                val bundle = Bundle()
                                bundle.putSerializable("adib", adib)
                                Navigation.findNavController(root)
                                    .navigate(R.id.aboutAdibFragment, bundle)
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
                                    firestore.collection("adib").document(adib.uid.toString())
                                        .set(adib)
                                } else {
                                    holder.itemAdibBinding.save.setBackgroundResource(R.drawable.save1)
                                    holder.itemAdibBinding.saveImg.setImageResource(R.drawable.ic_saqlanganlar)
                                    adib.isSave = true
                                    firestore.collection("adib").document(adib.uid.toString())
                                        .set(adib)
                                }
                            }
                        })
                    rv.adapter = adibAdapter
                }
                "O’zbek" -> {
                    loadData("O\'zbek")
                    adibAdapter = AdibAdapter(
                        requireContext(),
                        list,
                        object : AdibAdapter.OnItemClickListener {
                            override fun onItemClickListener(adib: Adib, position: Int) {
                                val bundle = Bundle()
                                bundle.putSerializable("adib", adib)
                                Navigation.findNavController(root)
                                    .navigate(R.id.aboutAdibFragment, bundle)
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
                                    firestore.collection("adib").document(adib.uid.toString())
                                        .set(adib)
                                } else {
                                    holder.itemAdibBinding.save.setBackgroundResource(R.drawable.save1)
                                    holder.itemAdibBinding.saveImg.setImageResource(R.drawable.ic_saqlanganlar)
                                    adib.isSave = true
                                    firestore.collection("adib").document(adib.uid.toString())
                                        .set(adib)
                                }
                            }
                        })
                    rv.adapter = adibAdapter
                }
                "Jahon" -> {
                    loadData("Jahon")
                    adibAdapter = AdibAdapter(
                        requireContext(),
                        list,
                        object : AdibAdapter.OnItemClickListener {
                            override fun onItemClickListener(adib: Adib, position: Int) {
                                val bundle = Bundle()
                                bundle.putSerializable("adib", adib)
                                Navigation.findNavController(root)
                                    .navigate(R.id.aboutAdibFragment, bundle)
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
                                    firestore.collection("adib").document(adib.uid.toString())
                                        .set(adib)
                                } else {
                                    holder.itemAdibBinding.save.setBackgroundResource(R.drawable.save1)
                                    holder.itemAdibBinding.saveImg.setImageResource(R.drawable.ic_saqlanganlar)
                                    adib.isSave = true
                                    firestore.collection("adib").document(adib.uid.toString())
                                        .set(adib)
                                }
                            }
                        })
                    rv.adapter = adibAdapter
                }
            }
        }
        return binding.root
    }

    private fun loadData(typeStr: String) {
        firestore.collection("adib")
            .get()
            .addOnSuccessListener { querySnapshot ->
                list.clear()
                querySnapshot.forEach { queryDocumentSnapshot ->
                    val adib = queryDocumentSnapshot.toObject(Adib::class.java)
                    adib.uid = queryDocumentSnapshot.id
                    if (adib.type.toString() == typeStr) {
                        list.add(adib)
                        if (list.isNotEmpty()) {
                            binding.lottie.visibility = View.INVISIBLE
                        } else {
                            binding.lottie.visibility = View.VISIBLE
                        }
                        adibAdapter.notifyDataSetChanged()
                    } else {
                        if (list.isNotEmpty()) {
                            binding.lottie.visibility = View.INVISIBLE
                        } else {
                            binding.lottie.visibility = View.VISIBLE
                        }
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            AdiblarViewPagerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}