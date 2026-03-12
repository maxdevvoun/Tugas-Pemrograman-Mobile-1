package com.example.sesi5

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sesi5.databinding.ActivityRegisterBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSpinner()
        setupRealTimeValidation()

        binding.btnRegister.setOnClickListener {
            if (validateForm()) {
                showConfirmationDialog()
            }
        }
    }

    private fun setupSpinner() {
        val jurusan = arrayOf("Informatika", "Sistem Informasi", "Teknik Komputer", "Data Science")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, jurusan)
        binding.spinnerJurusan.adapter = adapter
    }

    private fun setupRealTimeValidation() {
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateForm(isRealTime = true)
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        binding.etName.addTextChangedListener(watcher)
        binding.etEmail.addTextChangedListener(watcher)
        binding.etPassword.addTextChangedListener(watcher)
        binding.etConfirmPassword.addTextChangedListener(watcher)
    }

    private fun validateForm(isRealTime: Boolean = false): Boolean {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()

        var isValid = true

        // Name Validation
        if (name.isEmpty()) {
            if (!isRealTime) binding.tilName.error = "Nama wajib diisi"
            isValid = false
        } else {
            binding.tilName.error = null
        }

        // Email Validation
        if (email.isEmpty()) {
            if (!isRealTime) binding.tilEmail.error = "Email wajib diisi"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = "Format email tidak valid"
            isValid = false
        } else {
            binding.tilEmail.error = null
        }

        // Password Validation
        if (password.isEmpty()) {
            if (!isRealTime) binding.tilPassword.error = "Password wajib diisi"
            isValid = false
        } else if (password.length < 6) {
            binding.tilPassword.error = "Password minimal 6 karakter"
            isValid = false
        } else {
            binding.tilPassword.error = null
        }

        // Confirm Password Validation
        if (confirmPassword != password) {
            binding.tilConfirmPassword.error = "Password tidak cocok"
            isValid = false
        } else {
            binding.tilConfirmPassword.error = null
        }

        if (isRealTime) return isValid

        // Gender Validation
        if (binding.rgGender.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Pilih jenis kelamin", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        // Hobi Validation (Min 3)
        val selectedHobbiesCount = listOf(
            binding.cbCoding, binding.cbGaming, binding.cbMusic, 
            binding.cbSport, binding.cbTraveling
        ).count { it.isChecked }

        if (selectedHobbiesCount < 3) {
            Toast.makeText(this, "Pilih minimal 3 hobi", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        return isValid
    }

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Konfirmasi")
            .setMessage("Apakah data sudah benar?")
            .setPositiveButton("Ya") { _, _ ->
                Toast.makeText(this, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                finish()
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}