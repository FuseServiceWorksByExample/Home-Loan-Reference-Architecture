/*
 * Copyright 2013-2014 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.jboss.demo.loanmanagement;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import org.jboss.demo.loanmanagement.command.ProcessApplicationCommand;
import org.jboss.demo.loanmanagement.model.Account;
import org.jboss.demo.loanmanagement.model.Address;
import org.jboss.demo.loanmanagement.model.Application;
import org.jboss.demo.loanmanagement.model.AssetsAndLiabilities;
import org.jboss.demo.loanmanagement.model.Automobile;
import org.jboss.demo.loanmanagement.model.Borrower;
import org.jboss.demo.loanmanagement.model.BorrowerAddress;
import org.jboss.demo.loanmanagement.model.CashDeposit;
import org.jboss.demo.loanmanagement.model.Declarations;
import org.jboss.demo.loanmanagement.model.HousingExpense;
import org.jboss.demo.loanmanagement.model.Property;
import org.jboss.demo.loanmanagement.widget.ApplicationAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

/**
 * The loan application editor screen.
 */
public final class ApplicationScreen extends Activity implements PropertyChangeListener {

    private ApplicationAdapter adapter;
    private Application application;
    private ExpandableListView applicationEditor;

    Application constructTestApplication() {
        // TODO remmove this when done testing
        final Application loanApp = new Application();
        loanApp.setAmortizationType("ARM");
        loanApp.setDescription("This is a loan description");
        loanApp.setDownPaymentSource("This is my down payment source");
        loanApp.setInterestRate(5);
        loanApp.setLoanAmount(100000);
        loanApp.setNumberOfMonths(360);
        loanApp.setPurchaseType("Construction");
        loanApp.setType("FHA");

        final AssetsAndLiabilities assets = new AssetsAndLiabilities();
        assets.setCompletedType("Not_Jointly");
        {
            final Account account = new Account();
            account.setNumber("123456789");
            account.setAmount(2000.00);
            account.setDescription("Savings");
            final Address address = new Address();
            address.setCity("Daytona Beach");
            address.setCounty("Volusia");
            address.setLine1("100 Wall Street");
            address.setLine2("PO Box 200");
            address.setPostalCode("32129");
            address.setState("FL");
            account.setAddress(address);
            assets.addAccount(account);
        }
        {
            final Automobile car = new Automobile();
            car.setAmount(2000);
            car.setDescription("94 Saab 900");
            assets.addAutomobile(car);
        }
        {
            final Automobile car = new Automobile();
            car.setAmount(22000.0);
            car.setDescription("2014 Subaru Crosstrek");
            assets.addAutomobile(car);
        }
        {
            final CashDeposit deposit = new CashDeposit();
            deposit.setAmount(1000.1234);
            deposit.setDescription("Found in sofa cushions");
            assets.addCashDeposit(deposit);
        }
        {
            final CashDeposit deposit = new CashDeposit();
            deposit.setAmount(2000);
            deposit.setDescription("Penny collection");
            assets.addCashDeposit(deposit);
        }
        {
            final CashDeposit deposit = new CashDeposit();
            deposit.setAmount(3000.99);
            deposit.setDescription("From brother's piggy bank");
            assets.addCashDeposit(deposit);
        }
        loanApp.setAssetsAndLiabilities(assets);

        final List<Borrower> borrowers = new ArrayList<Borrower>();
        {
            final Borrower borrower = new Borrower();
            borrower.setFirstName("Elvis");
            borrower.setMiddleName("Declan");
            borrower.setLastName("Costello");
            borrower.setType("Borrower");
            final Declarations declarations = new Declarations();
            declarations.setAnyJudgments(true);
            declarations.setBorrowedDownPayment(true);
            declarations.setCoMakerNote(true);
            declarations.setDeclaredBankrupt(true);
            declarations.setDelinquent(true);
            declarations.setLawsuit(true);
            declarations.setObligatedOnAnyLoan(true);
            declarations.setObligatedToPayAlimony(true);
            declarations.setOwnershipInterest(true);
            declarations.setPermanentResident(true);
            declarations.setPrimaryResidence(true);
            declarations.setPropertyForeclosed(true);
            declarations.setPropertyType(Declarations.PROPERTY_TYPES[0]);
            declarations.setTitled(Declarations.TITLED_BY_TYPES[0]);
            declarations.setUsCitizen(true);
            borrower.setDeclarations(declarations);
            borrower.setDependentsAges(new int[] {5, 10, 15});
            borrower.setDob("01/01/1990");
            // TODO borrower.setEmploymentInformation(newEmploymentInformation);
            borrower.setMaritalStatus(Borrower.MARITAL_TYPE[0]);
            borrower.setNumberOfDependents(3); // TODO derive this from dependent ages maybe
            borrower.setPhone("123-456-7890");
            borrower.setSsn("111-11-1111");
            borrower.setTitle("King");
            borrower.setType(Borrower.BORROWER_TYPE[Borrower.BORROWER_INDEX]);
            borrower.setYearsSchool(16);

            final BorrowerAddress address = new BorrowerAddress();
            address.setCity("city");
            address.setCounty("county");
            address.setLine1("line1");
            address.setLine2("line2");
            address.setNumYears(5);
            address.setPostalCode("zip");
            address.setState("state");
            address.setType(BorrowerAddress.ADDRESS_TYPES[0]);
            borrower.addAddress(address);

            borrowers.add(borrower);
        }

        {
            final Borrower borrower = new Borrower();
            borrower.setFirstName("Paul");
            borrower.setMiddleName("B.");
            borrower.setLastName("Westerberg");
            borrower.setType("Co_Borrower");
            final Declarations declarations = new Declarations();
            declarations.setAnyJudgments(false);
            declarations.setBorrowedDownPayment(false);
            declarations.setCoMakerNote(false);
            declarations.setDeclaredBankrupt(false);
            declarations.setDelinquent(false);
            declarations.setLawsuit(false);
            declarations.setObligatedOnAnyLoan(false);
            declarations.setObligatedToPayAlimony(false);
            declarations.setOwnershipInterest(false);
            declarations.setPermanentResident(false);
            declarations.setPrimaryResidence(false);
            declarations.setPropertyForeclosed(false);
            declarations.setPropertyType(Declarations.PROPERTY_TYPES[1]);
            declarations.setTitled(Declarations.TITLED_BY_TYPES[1]);
            declarations.setUsCitizen(false);
            borrower.setDeclarations(declarations);
            borrower.setDependentsAges(new int[] {1, 2, 3, 4, 5});
            borrower.setDob("12/31/2001");
            // TODO borrower.setEmploymentInformation(newEmploymentInformation);
            borrower.setMaritalStatus(Borrower.MARITAL_TYPE[1]);
            borrower.setNumberOfDependents(5); // TODO derive this from dependent ages maybe
            borrower.setPhone("(314) 555-5555");
            borrower.setSsn("222-22-2222");
            borrower.setTitle("Mr");
            borrower.setType(Borrower.BORROWER_TYPE[Borrower.CO_BORROWER_INDEX]);
            borrower.setYearsSchool(12.5);

            {
                final BorrowerAddress address = new BorrowerAddress();
                address.setCity("Miami");
                address.setCounty("Volusia");
                address.setLine1("111 Main St");
                address.setLine2("c/o whoever");
                address.setNumYears(10);
                address.setPostalCode("32333");
                address.setState("Florida");
                address.setType(BorrowerAddress.ADDRESS_TYPES[1]);
                borrower.addAddress(address);
            }

            {
                final BorrowerAddress address = new BorrowerAddress();
                address.setCity("Jacksonville");
                address.setCounty("Washington");
                address.setLine1("222 Your Rd");
                address.setLine2("Suite 100");
                address.setNumYears(1.5);
                address.setPostalCode("356789");
                address.setState("Georgia");
                address.setType(BorrowerAddress.ADDRESS_TYPES[2]);
                borrower.addAddress(address);
            }

            borrowers.add(borrower);
        }

        {
            final Borrower borrower = new Borrower();
            borrower.setFirstName("Joey");
            borrower.setLastName("Ramone");
            borrower.setType("Co_Borrower");
            final Declarations declarations = new Declarations();
            declarations.setAnyJudgments(true);
            declarations.setBorrowedDownPayment(false);
            declarations.setCoMakerNote(true);
            declarations.setDeclaredBankrupt(false);
            declarations.setDelinquent(true);
            declarations.setLawsuit(false);
            declarations.setObligatedOnAnyLoan(true);
            declarations.setObligatedToPayAlimony(false);
            declarations.setOwnershipInterest(true);
            declarations.setPermanentResident(false);
            declarations.setPrimaryResidence(true);
            declarations.setPropertyForeclosed(false);
            declarations.setPropertyType(Declarations.PROPERTY_TYPES[2]);
            declarations.setTitled(Declarations.TITLED_BY_TYPES[2]);
            declarations.setUsCitizen(true);
            borrower.setDeclarations(declarations);
            borrower.setDependentsAges(new int[] {9});
            borrower.setDob("6/01/1921");
            // TODO borrower.setEmploymentInformation(newEmploymentInformation);
            borrower.setMaritalStatus(Borrower.MARITAL_TYPE[2]);
            borrower.setNumberOfDependents(5); // TODO derive this from dependent ages maybe
            borrower.setPhone("636343434");
            borrower.setSsn("333-33-3333");
            borrower.setTitle("Dr");
            borrower.setType(Borrower.BORROWER_TYPE[Borrower.CO_BORROWER_INDEX]);
            borrower.setYearsSchool(18);
            borrowers.add(borrower);
        }

        loanApp.setBorrowers(borrowers);

        final HousingExpense housingExpense = new HousingExpense();
        housingExpense.setFirstMortgage(1000.01);
        housingExpense.setHazardInsurance(200.50);
        housingExpense.setHomeOwnerAssociationDues(300.75);
        housingExpense.setOther(400.80);
        housingExpense.setOtherMortgages(555);
        housingExpense.setRealEstateTaxes(612.34);
        housingExpense.setRent(799.99);
        housingExpense.setType("Proposed");
        loanApp.setHousingExpense(housingExpense);

        final Property property = new Property();
        final Address address = new Address();
        address.setCity("Washington");
        address.setCounty("Warren");
        address.setLine1("1234 Main St");
        address.setLine2("Suite A");
        address.setPostalCode("63090");
        address.setState("Missouri");
        property.setAddress(address);
        property.setNumUnits(5);
        property.setType("Secondary_Residence");
        property.setYearBuilt(1959);
        loanApp.setProperty(property);

        return loanApp;
    }

    ApplicationAdapter getAdapter() {
        return this.adapter;
    }

    /**
     * Required by the <code>onClick</code> attribute in the XML file.
     * 
     * @param item the item for the collapse all groups action (never <code>null</code>)
     */
    public void handleCollapseAll( final MenuItem item ) {
        Log.d(ApplicationScreen.class.getSimpleName(), "handleCollapseAll called"); //$NON-NLS-1$
        this.adapter.collapseAllGroups();
    }

    /**
     * Required by the <code>onClick</code> attribute in the XML file.
     * 
     * @param item the item for the process application action (never <code>null</code>)
     */
    public void handleProcess( final MenuItem item ) {
        Log.d(ApplicationScreen.class.getSimpleName(), "handleProcess called"); //$NON-NLS-1$

        final ProcessApplicationCommand command = new ProcessApplicationCommand(this);
        command.execute(this.application);
    }

    /**
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate( final Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application);

        this.applicationEditor = (ExpandableListView)findViewById(R.id.application_expandable_list);
        this.application = constructTestApplication();
        this.application.add(this);
        this.adapter = new ApplicationAdapter(this, this.applicationEditor, this.application);
        this.applicationEditor.setAdapter(this.adapter);

        // add up arrow
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu( final Menu optionsMenu ) {
        getMenuInflater().inflate(R.menu.application_screen_menu, optionsMenu);
        return true;
    }

    /**
     * Expands the assets and liabilities section. Required by the <code>onClick</code> attribute of the xml file.
     * 
     * @param item the menu item (never <code>null</code>)
     */
    public void onJumpToAssets( final MenuItem item ) {
        this.applicationEditor.expandGroup(ApplicationAdapter.ASSETS_INDEX);
    }

    /**
     * Expands the borrowers section. Required by the <code>onClick</code> attribute of the xml file.
     * 
     * @param item the menu item (never <code>null</code>)
     */
    public void onJumpToBorrowers( final MenuItem item ) {
        this.applicationEditor.expandGroup(ApplicationAdapter.BORROWERS_INDEX);
    }

    /**
     * Expands the housing expense section. Required by the <code>onClick</code> attribute of the xml file.
     * 
     * @param item the menu item (never <code>null</code>)
     */
    public void onJumpToHousingExpense( final MenuItem item ) {
        this.applicationEditor.expandGroup(ApplicationAdapter.HOUSING_EXPENSE_INDEX);
    }

    /**
     * Expands the loan section. Required by the <code>onClick</code> attribute of the xml file.
     * 
     * @param item the menu item (never <code>null</code>)
     */
    public void onJumpToLoan( final MenuItem item ) {
        this.applicationEditor.expandGroup(ApplicationAdapter.LOAN_INDEX);
    }

    /**
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected( final MenuItem selectedItem ) {
        final int selectedItemId = selectedItem.getItemId();

        if (selectedItemId == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(selectedItem);
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange( final PropertyChangeEvent event ) {
        final String propName = event.getPropertyName();
        final Object oldValue = event.getOldValue();
        final Object newValue = event.getNewValue();

        runOnUiThread(new Thread(new Runnable() {

            /**
             * @see java.lang.Runnable#run()
             */
            @Override
            public void run() {
                if (Application.Properties.ASSETS_LIABILITIES.equals(propName)) {
                    if ((oldValue instanceof Account) || (newValue instanceof Account)) {
                        getAdapter().refreshAccounts();
                    } else if ((oldValue instanceof Automobile) || (newValue instanceof Automobile)) {
                        getAdapter().refreshAutos();
                    } else if ((oldValue instanceof CashDeposit) || (newValue instanceof CashDeposit)) {
                        getAdapter().refreshDeposits();
                    }
                } else if (AssetsAndLiabilities.Properties.ACCOUNTS.equals(propName)) {
                    getAdapter().refreshAccounts();
                } else if (AssetsAndLiabilities.Properties.AUTOS.equals(propName)) {
                    getAdapter().refreshAutos();
                } else if (AssetsAndLiabilities.Properties.CASH_DEPOSITS.equals(propName)) {
                    getAdapter().refreshDeposits();
                } else if (Application.Properties.BORROWERS.equals(propName)) {
                    getAdapter().refreshBorrowers();
                }
            }
        }));
    }

}
