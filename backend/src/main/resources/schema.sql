CREATE DATABASE IF NOT EXISTS discipline_ledger;
USE discipline_ledger;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(180) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_settings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL UNIQUE,
    initial_capital DECIMAL(19,2) NOT NULL,
    target_return_percentage DECIMAL(7,2) NOT NULL,
    max_trades_per_day INT NOT NULL,
    max_daily_loss_percentage DECIMAL(7,2) NOT NULL,
    motivation_message TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_settings_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS journal_entries (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    entry_date DATE NOT NULL,
    starting_capital DECIMAL(19,2) NOT NULL,
    ending_capital DECIMAL(19,2) NOT NULL,
    todays_profit_loss DECIMAL(19,2) NOT NULL,
    number_of_trades INT NOT NULL,
    mistakes_made TEXT,
    what_went_well TEXT,
    notes TEXT,
    mood_discipline_notes TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_journal_entries_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS rules_notes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL UNIQUE,
    trading_rules TEXT,
    discipline_rules TEXT,
    motivation_notes TEXT,
    daily_reminders TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_rules_notes_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS trades (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    journal_entry_id BIGINT NOT NULL,
    trade_date DATE NOT NULL,
    symbol VARCHAR(50) NOT NULL,
    side VARCHAR(10) NOT NULL,
    quantity DECIMAL(19,4) NOT NULL,
    entry_price DECIMAL(19,4) NOT NULL,
    exit_price DECIMAL(19,4) NOT NULL,
    profit_loss DECIMAL(19,2) NOT NULL,
    strategy VARCHAR(120),
    setup_quality DECIMAL(5,2),
    notes TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_trades_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_trades_journal_entry FOREIGN KEY (journal_entry_id) REFERENCES journal_entries(id)
);

INSERT IGNORE INTO users (id, name, email, password)
VALUES (1, 'Sandy', 'sandy@example.com', 'change-me');

INSERT IGNORE INTO user_settings (
    id,
    user_id,
    initial_capital,
    target_return_percentage,
    max_trades_per_day,
    max_daily_loss_percentage,
    motivation_message
)
VALUES (1, 1, 100000.00, 10.00, 8, 2.00, 'Protect capital first. Follow process, not impulse.');
