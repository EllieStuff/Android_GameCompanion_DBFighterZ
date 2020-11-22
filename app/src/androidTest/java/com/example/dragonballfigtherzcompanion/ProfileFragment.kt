package com.example.dragonballfigtherzcompanion

import android.app.Fragment

class ProfileFragment : Fragment {
    override fun onCreateView(inflater: LayoutInflater, contrainer: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, attachToRoot: false)
    }

    override fun onCreateView(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val registerButton:Button! = view.findViewById<Button>(R.id.registerButton)
        registerButton.setOnClickListener { it:View!
            val intent  = Intent(activity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}