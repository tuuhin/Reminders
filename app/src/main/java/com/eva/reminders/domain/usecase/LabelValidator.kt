package com.eva.reminders.domain.usecase

class LabelValidator {

    fun validate(label: String, others: List<String>): Validator {
        return when {
            label.isEmpty() -> Validator(isValid = false, "Empty labels are not allowed")
            others.contains(label) -> Validator(isValid = false, "Label already exits")
            else -> Validator(isValid = true)
        }
    }
}