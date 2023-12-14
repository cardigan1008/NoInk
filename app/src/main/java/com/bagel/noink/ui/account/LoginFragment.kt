package com.bagel.noink.ui.account

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bagel.noink.databinding.FragmentLoginBinding
import com.bagel.noink.utils.UserHttpRequest
import kotlinx.coroutines.launch
import org.json.JSONObject

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null

    // 声明组件对象
    private var usernameText: EditText? = null
    private var passwdText: EditText? = null
    private var loginButton: Button? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val loginViewModel =
            ViewModelProvider(this).get(LoginViewModel::class.java)
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // 绑定组件对象
        usernameText = binding.username
        passwdText = binding.Password
        loginButton = binding.loginButton

        // 按下登录按钮
        loginButton?.setOnClickListener {
            val username = usernameText!!.text.toString()
            val passwd = passwdText!!.text.toString()
            val userHttpRequest = UserHttpRequest()
            userHttpRequest.loginRequest(
                username = username,
                password = passwd,
                callbackListener = object : UserHttpRequest.UserCallbackListener {
                    @SuppressLint("ShowToast")
                    override fun onSuccess(responseJson: JSONObject) {
                        // 返回400，用户名或密码不正确，弹出弹窗提示
                        if (responseJson.getInt("code") == 400) {
                            lifecycleScope.launch {
                                Toast.makeText(requireContext(), "用户名或密码不正确", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            return
                        }

                        val data = responseJson.getJSONObject("data")
                        lifecycleScope.launch {
                            // 在主线程中安全地更新 ViewModel
                            AccountViewModel.updateUserInfoByJson(data)
                            // 写入token
                            val sharedPreferences = activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                            val editor = sharedPreferences?.edit()
                            editor?.putString("token", AccountViewModel.token)
                            editor?.apply()

                            // 登录成功，返回
                            activity?.finish()
                        }
                    }

                    override fun onFailure(errorMessage: String) {
                        // Nothing
                    }
                }
            )
        }
        return root
    }
}