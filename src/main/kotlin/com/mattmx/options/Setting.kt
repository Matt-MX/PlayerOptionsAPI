package com.mattmx.options

class Setting<T>(
    var id: String,
    val defaultOption: T
) {
    var value: T = defaultOption

    fun reset() = defaultOption

    operator fun invoke() = value
    operator fun invoke(newValue: T) = apply {
        this.value = newValue
    }
    fun get() = value
    fun set(n: T) {
        value = n
    }

    fun setAny(n: Any) {
        value = n as T? ?: return
    }

    fun clone() = Setting(id, defaultOption)
    fun cloneWithValue() = Setting(id, defaultOption).apply { this.value = this@Setting.value }

    override fun toString() = "$id<${defaultOption!!::class.java.simpleName}>(currently = $value, default = $defaultOption)"
}