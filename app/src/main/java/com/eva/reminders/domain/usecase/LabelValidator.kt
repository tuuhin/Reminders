package com.eva.reminders.domain.usecase

class LabelValidator {

    fun validate(label: String, others: List<String>): Validator {
        if (label.isEmpty()) return Validator(isValid = false, "Empty labels are not allowed")
        if (others.contains(label)) return Validator(isValid = false, "Label already exits")
        return Validator(isValid = true)
    }
}