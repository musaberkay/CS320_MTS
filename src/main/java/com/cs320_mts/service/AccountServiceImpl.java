package com.cs320_mts.service;

import com.cs320_mts.model.Account;
import com.cs320_mts.model.Transaction;
import com.cs320_mts.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService 
{

    @Autowired
    AccountRepository accountRepository;

    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public List<Account> getAll() {
        List<Account> accounts = accountRepository.findAll();
        return accounts;
    }

    @Override
    public Account getById(int id) {
        return accountRepository.findById(id).get();
    }

    @Override
    public void deleteById(int id) {
        accountRepository.deleteById(id);
    }

    @Override
    @Transactional
    public boolean moneyTransfer(int senderId, Transaction transaction) throws Exception {
        List<Integer> accountIdList = accountRepository.getAccountIdList();

        if(transaction.getAmount() <= 0)
        {
            throw new Exception("Please enter valid amount");
        }else if(!isContain(transaction.getRecipientAccId(),accountIdList))
        {
            throw new Exception("Please enter valid recipient id");
        }

        Account senderAccount = accountRepository.findById(senderId).get();
        if(senderAccount.getBalance() - transaction.getAmount() >= 0)
        {
            Account recipientAccount = accountRepository.findById(transaction.getRecipientAccId()).get();
            recipientAccount.setBalance(recipientAccount.getBalance() + transaction.getAmount());
            accountRepository.save(recipientAccount);

            senderAccount.setBalance(senderAccount.getBalance() - transaction.getAmount());
            senderAccount.getTransactions().add(transaction);
            accountRepository.save(senderAccount);

        }else
        {
            throw new Exception("Not enough account balance");
        }

        return true;
    }

    private boolean isContain(int id,List<Integer> accountIdList)
    {
        for (int someAccountId: accountIdList)
        {
            if(someAccountId == id)
                return true;
        }
        return false;
    }

	@Override
	public List<Account> getAccountsByUserId(int userId) {
		return accountRepository.getAccountsByUserId(userId);
	}
}
